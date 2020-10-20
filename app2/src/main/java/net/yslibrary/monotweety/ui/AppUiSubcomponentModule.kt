package net.yslibrary.monotweety.ui

import dagger.Module
import net.yslibrary.monotweety.ui.launcher.LauncherActivityComponent

@Module(
    subcomponents = [
        LauncherActivityComponent::class,
    ]
)
interface AppUiSubcomponentModule {
    interface ComponentProviders {
        fun launcherActivityComponent(): LauncherActivityComponent.Factory
    }
}
