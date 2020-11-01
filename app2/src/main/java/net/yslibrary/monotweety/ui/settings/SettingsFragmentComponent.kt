package net.yslibrary.monotweety.ui.settings

import dagger.Module
import dagger.Subcomponent
import net.yslibrary.monotweety.ui.footereditor.FooterEditorFragmentComponent

@Subcomponent(
    modules = [
        SettingsFragmentSubcomponentModule::class,
    ]
)
interface SettingsFragmentComponent : SettingsFragmentSubcomponentModule.ComponentProviders {
    fun inject(fragment: SettingsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun build(): SettingsFragmentComponent
    }

    interface ComponentProvider {
        fun settingsFragmentComponent(): Factory
    }
}

@Module(
    subcomponents = [
        FooterEditorFragmentComponent::class,
    ]
)
interface SettingsFragmentSubcomponentModule {
    interface ComponentProviders : FooterEditorFragmentComponent.ComponentProvider
}
