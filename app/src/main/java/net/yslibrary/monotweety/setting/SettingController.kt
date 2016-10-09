package net.yslibrary.monotweety.setting

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding.widget.checkedChanges
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.ActionBarController
import net.yslibrary.monotweety.base.HasComponent
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.logout.LogoutService
import net.yslibrary.monotweety.notification.NotificationService
import net.yslibrary.monotweety.setting.adapter.SettingAdapter
import net.yslibrary.monotweety.setting.adapter.SubHeaderDividerDecoration
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/09/24.
 */
class SettingController : ActionBarController(), HasComponent<SettingComponent> {

  lateinit var bindings: Bindings

  val adapter by lazy { SettingAdapter(applicationContext.resources, adapterListener) }

  val adapterListener = object : SettingAdapter.Listener {
    override fun onAppVersionClick() {

    }

    override fun onDeveloperClick() {

    }

    override fun onGooglePlayClick() {

    }

    override fun onHowtoClick() {

    }

    override fun onLicenseClick() {

    }

    override fun onKeepDialogOpenClick(enabled: Boolean) {
      viewModel.onKeepDialogOpenChanged(enabled)
    }

    override fun onLogoutClick() {
      viewModel.onLogoutRequested()
    }

    override fun onOpenProfileClick() {

    }
  }

  override val title: String?
    get() = getString(R.string.setting_title)

  override val component: SettingComponent by lazy {
    getComponentProvider<SettingComponent.ComponentProvider>(activity)
        .settingComponent(SettingViewModule())
  }

  @field:[Inject]
  lateinit var viewModel: SettingViewModel

  override fun onCreate() {
    super.onCreate()
    component.inject(this)
  }

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = inflater.inflate(R.layout.controller_setting, container, false)

    bindings = Bindings(view)

    bindings.list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    bindings.list.addItemDecoration(SubHeaderDividerDecoration(applicationContext))
    bindings.list.adapter = adapter

    setEvents()

    // twitter profile

    // start on reboot

    // how to

    // version info

    // license

    // developer

    // to google play

    // padding for ad?

    return view
  }

  fun setEvents() {
    // make sure to get saved status before subscribes to view events
    viewModel.notificationEnabledChanged
        .bindToLifecycle()
        .doOnNext { bindings.notificationSwitch.isChecked = it }
        .subscribe {
          Timber.d("notification enabled: $it")
          if (it) startNotificationService() else stopNotificationService()
        }

    viewModel.keepDialogOpen
        .first()
        .bindToLifecycle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { adapter.updateKeepDialogOpen(it) }

    viewModel.user
        .bindToLifecycle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          Timber.d("user: $it")
          it?.let { adapter.updateProfile(it) }
        }

    viewModel.logoutRequests
        .bindToLifecycle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { logout() }

    bindings.notificationSwitch
        .checkedChanges()
        .bindToLifecycle()
        .subscribe { viewModel.onNotificationEnabledChanged(it) }
  }

  fun startNotificationService() {
    applicationContext.startService(NotificationService.callingIntent(activity))
  }

  fun stopNotificationService() {
    applicationContext.stopService(NotificationService.callingIntent(activity))
  }

  fun logout() {
    val intent = LogoutService.callingIntent(applicationContext)
    applicationContext.startService(intent)

    activity.finish()
  }

  inner class Bindings(view: View) {
    val notificationSwitch = view.findById<SwitchCompat>(R.id.notification_switch)
    val list = view.findById<RecyclerView>(R.id.list)
  }
}