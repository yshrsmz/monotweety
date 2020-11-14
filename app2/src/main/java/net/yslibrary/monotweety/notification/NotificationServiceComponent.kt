package net.yslibrary.monotweety.notification

import dagger.Subcomponent
import net.yslibrary.monotweety.di.ServiceScope

@ServiceScope
@Subcomponent
interface NotificationServiceComponent {

    fun inject(service: NotificationService)

    @Subcomponent.Factory
    interface Factory {
        fun build(): NotificationServiceComponent
    }

    interface ComponentProvider {
        fun notificationServiceComponent(): Factory
    }
}
