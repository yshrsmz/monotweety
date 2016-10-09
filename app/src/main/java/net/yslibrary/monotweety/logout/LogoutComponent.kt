package net.yslibrary.monotweety.logout

import dagger.Component
import net.yslibrary.monotweety.UserComponent
import net.yslibrary.monotweety.base.di.ServiceScope

/**
 * Created by yshrsmz on 2016/10/09.
 */
@ServiceScope
@Component(
    dependencies = arrayOf(UserComponent::class)
)
interface LogoutComponent {
  fun inject(service: LogoutService)
}