package net.yslibrary.monotweety.setting

import dagger.Subcomponent
import net.yslibrary.monotweety.base.di.ControllerScope

/**
 * Created by yshrsmz on 2016/09/25.
 */
@ControllerScope
@Subcomponent(
    modules = arrayOf(SettingViewModule::class)
)
interface SettingComponent {
  fun inject(controller: SettingController)

  interface ComponentProvider {
    fun settingComponent(module: SettingViewModule): SettingComponent
  }
}