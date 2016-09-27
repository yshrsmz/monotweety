package net.yslibrary.monotweety.activity.main

import dagger.Component
import net.yslibrary.monotweety.AppComponent
import net.yslibrary.monotweety.activity.ActivityModule
import net.yslibrary.monotweety.base.di.ActivityScope
import net.yslibrary.monotweety.setting.SettingComponent

/**
 * Created by yshrsmz on 2016/09/25.
 */
@ActivityScope
@Component(
    dependencies = arrayOf(AppComponent::class),
    modules = arrayOf(ActivityModule::class)
)
interface MainActivityComponent : ActivityModule.Provider,
                                  SettingComponent.ComponentProvider {
  fun inject(activity: MainActivity)
}