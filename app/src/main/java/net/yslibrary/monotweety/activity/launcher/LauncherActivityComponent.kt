package net.yslibrary.monotweety.activity.launcher

import dagger.Component
import net.yslibrary.monotweety.AppComponent
import net.yslibrary.monotweety.activity.ActivityModule
import net.yslibrary.monotweety.base.di.ActivityScope
import net.yslibrary.monotweety.login.LoginComponent
import net.yslibrary.monotweety.setting.SettingViewModule
import net.yslibrary.monotweety.splash.SplashComponent

/**
 * Created by yshrsmz on 2016/09/27.
 */
@ActivityScope
@Component(
    dependencies = arrayOf(AppComponent::class),
    modules = arrayOf(ActivityModule::class)
)
interface LauncherActivityComponent : ActivityModule.Provider,
                                      SplashComponent.ComponentProvider,
                                      LoginComponent.ComponentProvider,
                                      SettingViewModule.DependencyProvider {
  fun inject(activity: LauncherActivity)
}