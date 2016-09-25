package net.yslibrary.monotweety.setting

import dagger.Component
import net.yslibrary.monotweety.UserComponent
import net.yslibrary.monotweety.base.di.ControllerScope

/**
 * Created by yshrsmz on 2016/09/25.
 */
@ControllerScope
@Component(
    dependencies = arrayOf(UserComponent::class),
    modules = arrayOf(SettingViewModule::class)
)
interface SettingViewComponent {
  fun inject(controller: SettingController)
}