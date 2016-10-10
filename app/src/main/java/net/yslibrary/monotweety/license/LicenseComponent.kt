package net.yslibrary.monotweety.license

import dagger.Component
import net.yslibrary.monotweety.UserComponent
import net.yslibrary.monotweety.base.di.ControllerScope

/**
 * Created by yshrsmz on 2016/10/10.
 */
@ControllerScope
@Component(
    dependencies = arrayOf(UserComponent::class),
    modules = arrayOf(LicenseViewModule::class)
)
interface LicenseComponent {
  fun inject(controller: LicenseController)
}