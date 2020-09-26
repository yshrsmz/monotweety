package net.yslibrary.monotweety.setting

import dagger.Component
import net.yslibrary.monotweety.UserComponent
import net.yslibrary.monotweety.base.di.ControllerScope

@ControllerScope
@Component(
    dependencies = [UserComponent::class],
    modules = [SettingViewModule::class]
)
interface SettingComponent {
    fun inject(controller: SettingController)
}
