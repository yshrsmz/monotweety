package net.yslibrary.monotweety.splash

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.SimpleSwapChangeHandler
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.ActionBarController
import net.yslibrary.monotweety.base.HasComponent
import net.yslibrary.monotweety.login.LoginController
import net.yslibrary.monotweety.login.LoginTransitionChangeHandler
import net.yslibrary.monotweety.setting.SettingController
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by yshrsmz on 2016/09/25.
 */
class SplashController : ActionBarController(), HasComponent<SplashComponent> {

  @field:[Inject]
  lateinit var viewModel: SplashViewModel

  override val shouldShowActionBar: Boolean = false

  override val component: SplashComponent by lazy {
    getComponentProvider<SplashComponent.ComponentProvider>(activity)
        .splashComponent(SplashViewModule())
  }

  override fun onCreate() {
    super.onCreate()
    component.inject(this)
    analytics.viewEvent(getString(R.string.title_splash))
  }

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = inflater.inflate(R.layout.controller_splash, container, false)

    Timber.d("onCreate")

    setEvents()

    return view
  }

  fun setEvents() {
    viewModel.loggedIn
        .zipWith(Observable.interval(2, TimeUnit.SECONDS).first()) { t1, t2 -> t1 }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          val next: Controller = if (it) SettingController() else LoginController()

          router.setRoot(RouterTransaction.with(next)
              .pushChangeHandler(if (it) SimpleSwapChangeHandler() else LoginTransitionChangeHandler())
              .popChangeHandler(if (it) SimpleSwapChangeHandler() else LoginTransitionChangeHandler()))
        }
  }
}