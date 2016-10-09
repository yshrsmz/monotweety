package net.yslibrary.monotweety.setting

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.ControllerScope
import net.yslibrary.monotweety.base.di.Names
import net.yslibrary.monotweety.setting.domain.KeepDialogOpenManager
import net.yslibrary.monotweety.setting.domain.NotificationEnabledManager
import net.yslibrary.monotweety.user.domain.GetUser
import net.yslibrary.rxeventbus.EventBus
import javax.inject.Named

/**
 * Created by yshrsmz on 2016/09/25.
 */
@Module
class SettingViewModule(private val activityBus: EventBus) {

  @ControllerScope
  @Provides
  @Named(Names.FOR_ACTIVITY)
  fun provideActivityBus(): EventBus = activityBus

  @ControllerScope
  @Provides
  fun provideSettingViewModel(notificationEnabledManager: NotificationEnabledManager,
                              getUser: GetUser,
                              keepDialogOpenManager: KeepDialogOpenManager): SettingViewModel {
    return SettingViewModel(notificationEnabledManager, getUser, keepDialogOpenManager)
  }

  interface DependencyProvider {
    @Named(Names.FOR_ACTIVITY)
    fun activityBus(): EventBus
  }
}