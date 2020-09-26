package net.yslibrary.monotweety.notification

import dagger.Component
import net.yslibrary.monotweety.UserComponent
import net.yslibrary.monotweety.base.di.ServiceScope

@ServiceScope
@Component(
    dependencies = [UserComponent::class],
    modules = [NotificationServiceModule::class]
)
interface NotificationComponent {
    fun inject(service: NotificationService)
}
