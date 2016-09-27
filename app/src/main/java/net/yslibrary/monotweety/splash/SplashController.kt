package net.yslibrary.monotweety.splash

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.BaseController
import net.yslibrary.monotweety.base.HasComponent
import timber.log.Timber
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

    return view
  }

  override fun onAttach(view: View) {
    super.onAttach(view)

    component.inject(this)
    setEvents()
  }

  fun setEvents() {

  }
}