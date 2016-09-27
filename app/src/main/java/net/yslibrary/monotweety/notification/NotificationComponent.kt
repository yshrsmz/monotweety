package net.yslibrary.monotweety.notification

import dagger.Component
import net.yslibrary.monotweety.UserComponent
import net.yslibrary.monotweety.base.di.ServiceScope

/**
 * Created by yshrsmz on 2016/09/26.
 */
@ServiceScope
@Component(
    dependencies = arrayOf(UserComponent::class),
    modules = arrayOf(NotificationServiceModule::class)
)
interface NotificationComponent {
  fun inject(service: NotificationService)
}