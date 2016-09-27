package net.yslibrary.monotweety.splash

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.BaseController
import net.yslibrary.monotweety.base.HasComponent
import net.yslibrary.monotweety.login.LoginController
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by yshrsmz on 2016/09/25.
 */
class SplashController : BaseController(), HasComponent<SplashComponent> {

  @field:[Inject]
  lateinit var viewModel: SplashViewModel

  override val component: SplashComponent by lazy {
    getComponentProvider<SplashComponent.ComponentProvider>(activity)
        .splashComponent(SplashViewModule())
  }

  override fun onCreate() {
    super.onCreate()
    component.inject(this)
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
          router.setRoot(RouterTransaction.with(LoginController())
              .popChangeHandler(FadeChangeHandler())
              .pushChangeHandler(FadeChangeHandler()))
        }
  }
}