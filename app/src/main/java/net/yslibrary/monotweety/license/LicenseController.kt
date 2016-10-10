package net.yslibrary.monotweety.license

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.yslibrary.licenseadapter.LicenseAdapter
import net.yslibrary.licenseadapter.LicenseEntry
import net.yslibrary.monotweety.App
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.ActionBarController
import net.yslibrary.monotweety.base.findById
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/10/10.
 */
class LicenseController : ActionBarController() {

  lateinit var bindings: Bindings

  @field:[Inject]
  lateinit var viewModel: LicenseViewModel

  override val title: String?
    get() = applicationContext.getString(R.string.license_title)

  val component: LicenseComponent by lazy {
    val activityBus = getComponentProvider<LicenseViewModule.DependencyProvider>(activity).activityBus()
    DaggerLicenseComponent.builder()
        .userComponent(App.userComponent(applicationContext))
        .licenseViewModule(LicenseViewModule(activityBus))
        .build()
  }

  override fun onCreate() {
    super.onCreate()
    component.inject(this)
  }

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = LayoutInflater.from(container.context).inflate(R.layout.controller_license, container, false)

    bindings = Bindings(view)

    setEvents()

    return view
  }

  fun setEvents() {
    viewModel.licenses
        .bindToLifecycle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { initAdapter(it) }

  }

  fun initAdapter(dataSet: List<LicenseEntry>) {
    bindings.list.layoutManager = LinearLayoutManager(activity)
    bindings.list.adapter = LicenseAdapter(dataSet)
  }

  class Bindings(view: View) {
    val list = view.findById<RecyclerView>(R.id.list)
  }
}