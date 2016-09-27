package net.yslibrary.monotweety.splash

import dagger.Subcomponent
import net.yslibrary.monotweety.base.di.ControllerScope

/**
 * Created by yshrsmz on 2016/09/27.
 */
@ControllerScope
@Subcomponent(
    modules = arrayOf(SplashViewModule::class)
)
interface SplashComponent {
  fun inject(controller: SplashController)

  interface ComponentProvider {
    fun splashComponent(module: SplashViewModule): SplashComponent
  }
}