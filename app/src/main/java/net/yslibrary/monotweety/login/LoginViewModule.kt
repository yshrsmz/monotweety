package net.yslibrary.monotweety.login

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.ControllerScope

/**
 * Created by yshrsmz on 2016/09/27.
 */
@Module
class LoginViewModule {

  @ControllerScope
  @Provides
  fun provideLoginViewModel(): LoginViewModel {
    return LoginViewModel()
  }
}