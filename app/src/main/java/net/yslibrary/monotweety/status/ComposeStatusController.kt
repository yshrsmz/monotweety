package net.yslibrary.monotweety.status

import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.ActionBarController
import net.yslibrary.monotweety.base.HasComponent
import net.yslibrary.monotweety.base.ProgressController
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.status.adapter.ComposeStatusAdapter
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
      viewModel.onStatusUpdated(status)
    }
  }

  lateinit var bindings: Bindings

  val adapter: ComposeStatusAdapter by lazy { ComposeStatusAdapter(adapterListener)  }

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
    // fill initial status string
    viewModel.status
        .first()
        .filter { it.isNotBlank() }
        .bindToLifecycle()
        .subscribe {
          bindings.statusInput.setText(it, TextView.BufferType.EDITABLE)
        }

    viewModel.keepDialogOpen
        .bindToLifecycle()
        .subscribe {
          if (bindings.keepDialogOpenSwitch.isChecked != it) {
            bindings.keepDialogOpenSwitch.isChecked = it
          }
        }

    // reset EditText
    viewModel.statusUpdated
        .bindToLifecycle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { bindings.statusInput.setText("", TextView.BufferType.EDITABLE) }

    viewModel.closeViewRequests
        .bindToLifecycle()
        .subscribe { activity?.finish() }

    viewModel.isSendableStatus
        .bindToLifecycle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { activity?.invalidateOptionsMenu() }

    viewModel.statusLength
        .bindToLifecycle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { updateStatusCounter(it.valid, it.length, it.maxLength) }

    viewModel.progressEvents
        .skip(1)
        .bindToLifecycle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          when (it) {
            ComposeStatusViewModel.ProgressEvent.IN_PROGRESS -> showLoadingState()
            ComposeStatusViewModel.ProgressEvent.FINISHED -> hideLoadingState()
          }
        }

    viewModel.messages.bindToLifecycle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { toastLong(it).show() }

    sendButtonClicks.bindToLifecycle()
        .subscribe { viewModel.onSendStatus() }

    bindings.statusInput.textChanges()
        .bindToLifecycle()
        .subscribe { viewModel.onStatusUpdated(it.toString()) }

    bindings.keepDialogOpenSwitch.checkedChanges()
        .bindToLifecycle()
        .subscribe { viewModel.onKeepDialogOpenChanged(it) }

    bindings.enableThreadSwitch.checkedChanges()
        .bindToLifecycle()
        .subscribe { viewModel.onEnableThreadChanged(it) }
  }

  fun initToolbar() {
    actionBar?.apply {
      setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
    }
  }

  fun updateStatusCounter(valid: Boolean, length: Int, maxLength: Int) {
    bindings.statusCounter.text = "$length/$maxLength"
    val colorResId = if (valid) R.color.colorTextSecondary else R.color.red
    bindings.statusCounter.setTextColor(ContextCompat.getColor(applicationContext, colorResId))
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
    //    val statusInput = view.findById<TextInputEditText>(R.id.status_input)
//    val statusCounter = view.findById<TextView>(R.id.status_counter)
//    val keepDialogOpenSwitch = view.findById<SwitchCompat>(R.id.keep_dialog)
//    val enableThreadSwitch = view.findById<SwitchCompat>(R.id.enable_thread)
    val list = view.findById<RecyclerView>(R.id.list)
    val overlayRoot = view.findById<FrameLayout>(R.id.overlay_root)
  }
}