package net.yslibrary.monotweety.status

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.ControllerScope
import net.yslibrary.monotweety.status.domain.CheckStatusLength

/**
 * Created by yshrsmz on 2016/10/02.
 */
@Module
class ComposeStatusViewModule {

  @ControllerScope
  @Provides
  fun provideComposeStatusViewModel(checkStatusLength: CheckStatusLength): ComposeStatusViewModel {
    return ComposeStatusViewModel(checkStatusLength)
  }
}