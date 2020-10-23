package net.yslibrary.monotweety.ui

import dagger.Module
import net.yslibrary.monotweety.ui.main.MainActivityComponent

@Module(
    subcomponents = [
        MainActivityComponent::class
    ]
)
interface UserUiSubcomponentModule {
    interface ComponentProviders : MainActivityComponent.ComponentProvider
}
