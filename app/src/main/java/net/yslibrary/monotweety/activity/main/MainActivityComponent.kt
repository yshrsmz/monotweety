package net.yslibrary.monotweety.activity.main

import dagger.Component
import net.yslibrary.monotweety.AppComponent
import net.yslibrary.monotweety.activity.ActivityModule
import net.yslibrary.monotweety.base.di.ActivityScope
import net.yslibrary.monotweety.changelog.ChangelogViewModule
import net.yslibrary.monotweety.license.LicenseViewModule
import net.yslibrary.monotweety.login.LoginComponent
import net.yslibrary.monotweety.setting.SettingViewModule
import net.yslibrary.monotweety.splash.SplashComponent

@ActivityScope
@Component(
    dependencies = arrayOf(AppComponent::class),
    modules = arrayOf(ActivityModule::class)
)
interface MainActivityComponent : ActivityModule.Provider,
    SplashComponent.ComponentProvider,
    LoginComponent.ComponentProvider,
    SettingViewModule.DependencyProvider,
    LicenseViewModule.DependencyProvider,
    ChangelogViewModule.DependencyProvider {
    fun inject(activity: MainActivity)
}
