package net.yslibrary.monotweety.login

import dagger.Subcomponent
import net.yslibrary.monotweety.base.di.ControllerScope

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