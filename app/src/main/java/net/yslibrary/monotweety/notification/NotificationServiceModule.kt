package net.yslibrary.monotweety.notification

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.ServiceScope

/**
 * Created by yshrsmz on 2016/09/26.
 */
@Module
class NotificationServiceModule(private val service: NotificationService) {

  @ServiceScope
  @Provides
  fun provideNotificationServicePresenter(): NotificationServiceContract.Presenter {
    return NotificationServicePresenter()
  }

  @ServiceScope
  @Provides
  fun provideNotificationServiceView(): NotificationServiceContract.View {
    return service
  }
}