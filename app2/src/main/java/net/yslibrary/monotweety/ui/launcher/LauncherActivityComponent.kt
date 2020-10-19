package net.yslibrary.monotweety.ui.launcher

import android.app.Activity
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import net.yslibrary.monotweety.di.ActivityScope
import net.yslibrary.monotweety.ui.splash.SplashFragmentComponent
import net.yslibrary.monotweety.ui.splash.SplashFragmentModule

@ActivityScope
@Subcomponent(
    modules = [
        SplashFragmentModule::class
    ]
)
interface LauncherActivityComponent : SplashFragmentComponent.ComponentProvider {
    fun inject(activity: LauncherActivity)

    @Subcomponent.Factory
    interface Factory {
        fun build(@BindsInstance activity: Activity): LauncherActivityComponent
    }

    interface ComponentProvider {
        fun launcherActivityComponent(): Factory
    }
}

@Module(
    subcomponents = [
        LauncherActivityComponent::class,
    ]
)
interface LauncherActivityComponentModule {

}
