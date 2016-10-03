package net.yslibrary.monotweety.status

import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SwitchCompat
import android.view.*
import android.widget.TextView
import com.jakewharton.rxbinding.widget.checkedChanges
import com.jakewharton.rxbinding.widget.textChanges
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.ActionBarController
import net.yslibrary.monotweety.base.HasComponent
import net.yslibrary.monotweety.base.findById
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.PublishSubject
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/10/01.
 */
class ComposeStatusController(private var status: String? = null) : ActionBarController(),
                                                                    HasComponent<ComposeStatusComponent> {

  init {
    setHasOptionsMenu(true)
  }

  override val component: ComposeStatusComponent by lazy {
    getComponentProvider<ComposeStatusComponent.ComponentProvider>(activity)
        .composeStatusComponent(ComposeStatusViewModule(status))
  }

  lateinit var bindings: Bindings

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

    // reset EditText
    viewModel.statusUpdated
        .bindToLifecycle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { bindings.statusInput.setText("", TextView.BufferType.EDITABLE) }

    viewModel.isSendableStatus
        .bindToLifecycle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { activity?.invalidateOptionsMenu() }

    viewModel.statusLength
        .bindToLifecycle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { updateStatusCounter(it.valid, it.length, it.maxLength) }

    sendButtonClicks.bindToLifecycle()
        .subscribe { viewModel.onSendStatus() }

    bindings.statusInput.textChanges()
        .bindToLifecycle()
        .map { it.toString() }
        .subscribe { viewModel.onStatusUpdated(it) }

    bindings.keepDialogSwitch.checkedChanges()
        .bindToLifecycle()
        .subscribe { viewModel.onKeepDialogChanged(it) }

    bindings.enableThreadSwitch.checkedChanges()
        .bindToLifecycle()
        .subscribe { viewModel.onEnableThreadChanged(it) }
  }

  fun initToolbar() {
    actionBar?.let {
      it.setDisplayHomeAsUpEnabled(true)
      it.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
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

    viewModel.isSendableStatus
        .first()
        .toBlocking()
        .subscribe { menu.findItem(R.id.action_send_tweet)?.isEnabled = it }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val id = item.itemId
    when (id) {
      R.id.action_send_tweet -> {
        Timber.d("option - action_send_tweet")
        sendButtonClicks.onNext(Unit)
      }
      android.R.id.home -> {
        Timber.d("option - home")
        activity.onBackPressed()
      }
    }

    return super.onOptionsItemSelected(item)
  }

  override fun handleBack(): Boolean {
    Timber.d("handleBack")
    return super.handleBack()
  }

  inner class Bindings(view: View) {
    val statusInput = view.findById<TextInputEditText>(R.id.status_input)
    val statusCounter = view.findById<TextView>(R.id.status_counter)
    val keepDialogSwitch = view.findById<SwitchCompat>(R.id.keep_dialog)
    val enableThreadSwitch = view.findById<SwitchCompat>(R.id.enable_thread)
  }
}