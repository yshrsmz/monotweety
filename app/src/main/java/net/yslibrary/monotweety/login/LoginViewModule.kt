package net.yslibrary.monotweety.login

import com.twitter.sdk.android.core.TwitterSession
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.ControllerScope
import net.yslibrary.monotweety.base.di.Names
import rx.subjects.PublishSubject
import javax.inject.Named

@Module
class LoginViewModule {

  @ControllerScope
  @Provides
  fun provideLoginViewModel(@Named(Names.FOR_LOGIN) loginCompletedSubject: PublishSubject<TwitterSession>): LoginViewModel {
    return LoginViewModel(loginCompletedSubject)
  }
}