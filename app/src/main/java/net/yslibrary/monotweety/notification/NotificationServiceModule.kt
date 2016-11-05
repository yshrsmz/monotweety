package net.yslibrary.monotweety.notification

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.ServiceScope
import net.yslibrary.monotweety.setting.domain.FooterStateManager
import net.yslibrary.monotweety.setting.domain.KeepOpenManager
import net.yslibrary.monotweety.setting.domain.NotificationEnabledManager
import net.yslibrary.monotweety.status.domain.CheckStatusLength
import net.yslibrary.monotweety.status.domain.UpdateStatus

/**
 * Created by yshrsmz on 2016/09/26.
 */
@Module
class NotificationServiceModule(private val service: NotificationService) {

  @ServiceScope
  @Provides
  fun provideNotificationServiceViewModel(notificationEnabledManager: NotificationEnabledManager,
                                          keepOpenManager: KeepOpenManager,
                                          checkStatusLength: CheckStatusLength,
                                          updateStatus: UpdateStatus,
                                          footerStateManager: FooterStateManager): NotificationServiceViewModel {
    return NotificationServiceViewModel(notificationEnabledManager, keepOpenManager, checkStatusLength, updateStatus, footerStateManager)
  }
}