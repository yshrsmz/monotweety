package net.yslibrary.monotweety.status

import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.util.Pair
import android.view.*
import android.widget.FrameLayout
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.ActionBarController
import net.yslibrary.monotweety.base.HasComponent
import net.yslibrary.monotweety.base.ProgressController
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.status.adapter.ComposeStatusAdapter
import net.yslibrary.monotweety.status.adapter.EditorAdapterDelegate
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.PublishSubject
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by yshrsmz on 2016/10/01.
 */
class ComposeStatusController(private var status: String? = null) : ActionBarController(),
                                                                    HasComponent<ComposeStatusComponent> {
  override val hasBackButton: Boolean = true
  override val hasOptionsMenu: Boolean = true

  override val component: ComposeStatusComponent by lazy {
    getComponentProvider<ComposeStatusComponent.ComponentProvider>(activity)
        .composeStatusComponent(ComposeStatusViewModule(status))
  }

  val adapterListener = object : ComposeStatusAdapter.Listener {
    override fun onEnableThreadChanged(enabled: Boolean) {
      viewModel.onEnableThreadChanged(enabled)
    }

    override fun onKeepDialogOpenChanged(enabled: Boolean) {
      viewModel.onKeepDialogOpenChanged(enabled)
    }

    override fun onStatusChanged(status: String) {
      viewModel.onStatusChanged(status)
    }
  }

  lateinit var bindings: Bindings

  val adapter: ComposeStatusAdapter by lazy { ComposeStatusAdapter(adapterListener) }

  @field:[Inject]
  lateinit var viewModel: ComposeStatusViewModel

  val sendButtonClicks = PublishSubject<Unit>()

  override fun onCreate() {
    super.onCreate()
    Timber.d("status: $status")
    component.inject(this)
  }

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
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
    if (bindings.list.itemAnimator is SimpleItemAnimator) {
      (bindings.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }
    bindings.list.adapter = adapter
    bindings.list.layoutManager = LinearLayoutManager(activity)

    Observable.combineLatest(
        viewModel.statusInfo.distinctUntilChanged(),
        viewModel.keepDialogOpen,
        viewModel.tweetAsThread,
        { statusInfo, keepDialogOpen, tweetAsThread ->
          EditorAdapterDelegate.Item(status = statusInfo.status,
              keepDialogOpen = keepDialogOpen,
              enableThread = tweetAsThread,
              statusLength = statusInfo.length,
              maxLength = statusInfo.maxLength,
              valid = statusInfo.valid,
              clear = false)
        })
        .distinctUntilChanged()
        .bindToLifecycle()
        .subscribe {
          Timber.d("item updated: $it")
          adapter.updateEditor(it)
        }

    viewModel.closeViewRequests
        .bindToLifecycle()
        .subscribe { activity?.finish() }

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
        .zipWith(viewModel.previousStatus, { unit, tweet -> tweet })
        .bindToLifecycle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          adapter.updatePreviousTweetAndClearEditor(if (it == null) emptyList() else listOf(it))
        }

    viewModel.messages.bindToLifecycle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { toastLong(it).show() }

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

    Observable.zip(
        viewModel.isSendableStatus,
        viewModel.progressEvents,
        { sendable, progress -> Pair(sendable, progress) })
        .first()
        .toBlocking()
        .subscribe {
          menu.findItem(R.id.action_send_tweet)?.isEnabled = it.first && it.second == ComposeStatusViewModel.ProgressEvent.FINISHED
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
      return super.handleBack()
    }

    showConfirmCloseDialog()

    return true
  }

  fun showConfirmCloseDialog() {

    AlertDialog.Builder(activity)
        .setTitle(R.string.title_cancel_confirm)
        .setMessage(R.string.label_cancel_confirm)
        .setCancelable(true)
        .setNegativeButton(
            R.string.label_no,
            { dialog, which ->
              viewModel.onConfirmCloseView(allowCloseView = false)
              dialog.dismiss()
            })
        .setPositiveButton(
            R.string.label_quit,
            { dialog, which ->
              viewModel.onConfirmCloseView(allowCloseView = true)
              activity.onBackPressed()
            }).show()
  }

  fun showLoadingState() {
    activity.invalidateOptionsMenu()
    getChildRouter(bindings.overlayRoot, null)
        .setPopsLastView(true)
        .setRoot(RouterTransaction.with(ProgressController())
            .popChangeHandler(FadeChangeHandler())
            .pushChangeHandler(FadeChangeHandler()))
  }

  fun hideLoadingState() {
    val childRouter = getChildRouter(bindings.overlayRoot, null)
    if (childRouter.backstackSize == 0) {
      return
    }

    activity.invalidateOptionsMenu()
    childRouter.popCurrentController()
  }

  inner class Bindings(view: View) {
    val list = view.findById<RecyclerView>(R.id.list)
    val overlayRoot = view.findById<FrameLayout>(R.id.overlay_root)
  }
}