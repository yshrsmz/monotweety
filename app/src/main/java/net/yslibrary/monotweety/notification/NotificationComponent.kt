package net.yslibrary.monotweety.notification

import dagger.Component
import net.yslibrary.monotweety.UserComponent
import net.yslibrary.monotweety.base.di.ServiceScope

@ServiceScope
@Component(
    dependencies = arrayOf(UserComponent::class),
    modules = arrayOf(NotificationServiceModule::class)
)
interface NotificationComponent {
    fun inject(service: NotificationService)
}
