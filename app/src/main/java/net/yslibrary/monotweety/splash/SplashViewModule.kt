package net.yslibrary.monotweety.splash

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.ControllerScope
import net.yslibrary.monotweety.login.domain.IsLoggedIn

@Module
class SplashViewModule {

  @ControllerScope
  @Provides
  fun provideSplashViewModel(isLoggedIn: IsLoggedIn): SplashViewModel {
    return SplashViewModel(isLoggedIn)
  }
}