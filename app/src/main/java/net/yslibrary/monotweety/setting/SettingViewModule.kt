package net.yslibrary.monotweety.setting

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.ControllerScope
import net.yslibrary.monotweety.setting.domain.NotificationEnabledManager
import net.yslibrary.monotweety.user.domain.GetUser

/**
 * Created by yshrsmz on 2016/09/25.
 */
@Module
class SettingViewModule() {

  @ControllerScope
  @Provides
  fun provideSettingViewModel(notificationEnabledManager: NotificationEnabledManager,
                              getUser: GetUser): SettingViewModel {
    return SettingViewModel(notificationEnabledManager, getUser)
  }
}