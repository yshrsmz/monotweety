package net.yslibrary.monotweety.notification

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.ServiceScope
import net.yslibrary.monotweety.setting.domain.NotificationEnabledManager

/**
 * Created by yshrsmz on 2016/09/26.
 */
@Module
class NotificationServiceModule(private val service: NotificationService) {

  @ServiceScope
  @Provides
  fun provideNotificationServiceViewModel(notificationEnabledManager: NotificationEnabledManager): NotificationServiceViewModel {
    return NotificationServiceViewModel(notificationEnabledManager)
  }
}