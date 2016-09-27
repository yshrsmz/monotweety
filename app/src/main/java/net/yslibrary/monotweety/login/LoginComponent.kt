package net.yslibrary.monotweety.login

import dagger.Subcomponent
import net.yslibrary.monotweety.base.di.ControllerScope

/**
 * Created by yshrsmz on 2016/09/27.
 */
@ControllerScope
@Subcomponent(
    modules = arrayOf(LoginViewModule::class)
)
interface LoginComponent {
  fun inject(controller: LoginController)

  interface ComponentProvider {
    fun loginComponent(module: LoginViewModule): LoginComponent
  }
}