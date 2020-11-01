package net.yslibrary.monotweety.ui.settings

import dagger.Subcomponent

@Subcomponent
interface SettingsFragmentComponent {
    fun inject(fragment: SettingsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun build(): SettingsFragmentComponent
    }

    interface ComponentProvider {
        fun settingsFragmentComponent(): Factory
    }
}

