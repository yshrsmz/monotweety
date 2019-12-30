package net.yslibrary.monotweety.status

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ShortcutManager
import android.os.Build
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.analytics.Analytics
import net.yslibrary.monotweety.base.ActionBarController
import net.yslibrary.monotweety.base.HasComponent
import net.yslibrary.monotweety.base.ObjectWatcherDelegate
import net.yslibrary.monotweety.base.ProgressController
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.base.hideKeyboard
import net.yslibrary.monotweety.status.adapter.ComposeStatusAdapter
import net.yslibrary.monotweety.status.adapter.EditorAdapterDelegate
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates


class ComposeStatusController(private var status: String? = null) : ActionBarController(),
    HasComponent<ComposeStatusComponent> {

    override val hasBackButton: Boolean = true
    override val hasOptionsMenu: Boolean = true

    override val component: ComposeStatusComponent by lazy {
        getComponentProvider<ComposeStatusComponent.ComponentProvider>(activity!!)
            .composeStatusComponent(ComposeStatusViewModule(status))
    }

    val adapterListener = object : ComposeStatusAdapter.Listener {
        override fun onStatusChanged(status: String) {
            viewModel.onStatusChanged(status)
        }
    }

    lateinit var bindings: Bindings

    val statusAdapter: ComposeStatusAdapter by lazy { ComposeStatusAdapter(adapterListener) }

    @set:[Inject]
    var viewModel by Delegates.notNull<ComposeStatusViewModel>()

    @set:[Inject]
    var objectWatcherDelegate by Delegates.notNull<ObjectWatcherDelegate>()

    val sendButtonClicks: PublishSubject<Unit> = PublishSubject.create<Unit>()

    override fun onContextAvailable(context: Context) {
        super.onContextAvailable(context)
        Timber.d("status: $status")
        component.inject(this)

        reportShortcutUsedIfNeeded(status)
        analytics.viewEvent(Analytics.VIEW_COMPOSE_STATUS)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_compose_status, container, false)

        bindings = Bindings(view)

        setEvents()

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        initToolbar()
    }

    fun setEvents() {

        // https://code.google.com/p/android/issues/detail?id=161559
        // disable animation to avoid duplicated viewholder
        bindings.list.apply {
            if (itemAnimator is SimpleItemAnimator) {
                (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            }
            adapter = statusAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        // FIXME: this should be handled in ViewModel
        viewModel.statusInfo.distinctUntilChanged()
            .map { (status1, valid, length, maxLength) ->
                EditorAdapterDelegate.Item(
                    status = status1,
                    statusLength = length,
                    maxLength = maxLength,
                    valid = valid,
                    clear = false
                )
            }
            .distinctUntilChanged()
            .bindToLifecycle()
            .subscribeBy {
                Timber.d("item updated: $it")
                statusAdapter.updateEditor(it)
            }

        viewModel.closeViewRequests
            .bindToLifecycle()
            .subscribe {
                view?.hideKeyboard()
                activity?.finish()
            }

        viewModel.isSendableStatus
            .bindToLifecycle()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { activity?.invalidateOptionsMenu() }

        viewModel.progressEvents
            .skip(1)
            .bindToLifecycle()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                when (it) {
                    ComposeStatusViewModel.ProgressEvent.IN_PROGRESS -> showLoadingState()
                    ComposeStatusViewModel.ProgressEvent.FINISHED -> hideLoadingState()
                    else -> hideLoadingState()
                }
            }

        viewModel.statusUpdated
            .bindToLifecycle()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Timber.d("status updated")
                toast(getString(R.string.message_tweet_succeeded))?.show()
                analytics.tweetFromEditor()
            }

        viewModel.messages.bindToLifecycle()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { toastLong(it)?.show() }

        sendButtonClicks.bindToLifecycle()
            .subscribe { viewModel.onSendStatus() }
    }

    fun initToolbar() {
        actionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_compose_status, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        Observables.zip(
            viewModel.isSendableStatus,
            viewModel.progressEvents
        ) { sendable, progress -> sendable to progress }
            .blockingFirst()
            .let {
                menu.findItem(R.id.action_send_tweet)?.isEnabled =
                    it.first && it.second == ComposeStatusViewModel.ProgressEvent.FINISHED
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_send_tweet -> {
                Timber.d("option - action_send_tweet")
                sendButtonClicks.onNext(Unit)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun handleBack(): Boolean {
        Timber.d("handleBack")
        if (viewModel.canClose) {
            view?.hideKeyboard()
            return super.handleBack()
        }

        showConfirmCloseDialog()

        return true
    }

    override fun onChangeEnded(
        changeHandler: ControllerChangeHandler,
        changeType: ControllerChangeType
    ) {
        super.onChangeEnded(changeHandler, changeType)
        objectWatcherDelegate.handleOnChangeEnded(isDestroyed, changeType)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
        objectWatcherDelegate.handleOnDestroy()
    }

    fun showConfirmCloseDialog() {
        activity?.let {
            Timber.tag("Dialog").i("showConfirmationCloseDialog")
            AlertDialog.Builder(it)
                .setTitle(R.string.label_confirm)
                .setMessage(R.string.label_cancel_confirm)
                .setCancelable(true)
                .setNegativeButton(R.string.label_no) { dialog, _ ->
                    viewModel.onConfirmCloseView(allowCloseView = false)
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.label_quit) { _, _ ->
                    viewModel.onConfirmCloseView(allowCloseView = true)
                    view?.hideKeyboard()
                    activity?.onBackPressed()
                }.show()
        }
    }

    fun showLoadingState() {
        activity?.invalidateOptionsMenu()
        getChildRouter(bindings.overlayRoot, null)
            .setPopsLastView(true)
            .setRoot(
                RouterTransaction.with(ProgressController())
                    .popChangeHandler(FadeChangeHandler())
                    .pushChangeHandler(FadeChangeHandler())
            )
    }

    fun hideLoadingState() {
        val childRouter = getChildRouter(bindings.overlayRoot, null)
        if (childRouter.backstackSize == 0) {
            return
        }

        activity?.invalidateOptionsMenu()
        childRouter.popCurrentController()
    }

    @SuppressLint("NewApi")
    fun reportShortcutUsedIfNeeded(status: String?) {
        if (status.isNullOrEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                applicationContext?.getSystemService(ShortcutManager::class.java)
                    ?.reportShortcutUsed("newtweet")
            }
        }
    }

    inner class Bindings(view: View) {
        val list = view.findById<RecyclerView>(R.id.list)
        val overlayRoot = view.findById<FrameLayout>(R.id.overlay_root)
    }
}
