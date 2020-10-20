package net.yslibrary.monotweety.ui.launcher

import android.app.Activity
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import net.yslibrary.monotweety.di.ActivityScope
import net.yslibrary.monotweety.ui.login.LoginFragmentComponent
import net.yslibrary.monotweety.ui.splash.SplashFragmentComponent

@ActivityScope
@Subcomponent(
    modules = [
        LauncherActivitySubcomponentModule::class
    ]
)
interface LauncherActivityComponent : LauncherActivitySubcomponentModule.ComponentProviders {
    fun inject(activity: LauncherActivity)

    @Subcomponent.Factory
    interface Factory {
        fun build(@BindsInstance activity: Activity): LauncherActivityComponent
    }
}

@Module(
    subcomponents = [
        SplashFragmentComponent::class,
        LoginFragmentComponent::class,
    ]
)
interface LauncherActivitySubcomponentModule {
    interface ComponentProviders {
        fun loginFragmentComponent(): LoginFragmentComponent.Factory
        fun splashFragmentComponent(): SplashFragmentComponent.Factory
    }
}
