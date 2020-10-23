package net.yslibrary.monotweety.ui.main

import android.app.Activity
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import net.yslibrary.monotweety.di.ActivityScope
import net.yslibrary.monotweety.ui.settings.SettingsFragmentComponent

@ActivityScope
@Subcomponent(
    modules = [
        MainActivitySubcomponentModule::class,
    ]
)
interface MainActivityComponent {
    fun inject(activity: MainActivity)

    @Subcomponent.Factory
    interface Factory {
        fun build(@BindsInstance activity: Activity): MainActivityComponent
    }

    interface ComponentProvider {
        fun mainActivityComponent(): Factory
    }
}

@Module(
    subcomponents = [
        SettingsFragmentComponent::class,
    ]
)
interface MainActivitySubcomponentModule {
    interface ComponentProviders : SettingsFragmentComponent.ComponentProvider
}
