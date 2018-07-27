package net.yslibrary.monotweety.logout

import dagger.Component
import net.yslibrary.monotweety.UserComponent
import net.yslibrary.monotweety.base.di.ServiceScope

@ServiceScope
@Component(
    dependencies = arrayOf(UserComponent::class)
)
interface LogoutComponent {
    fun inject(service: LogoutService)
}
