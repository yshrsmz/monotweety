package net.yslibrary.monotweety.ui.compose

import dagger.Subcomponent
import net.yslibrary.monotweety.di.FragmentScope

@FragmentScope
@Subcomponent
interface ComposeStatusDialogFragmentComponent {
    fun inject(fragment: ComposeStatusDialogFragment)

    @Subcomponent.Factory
    interface Factory {
        fun build(): ComposeStatusDialogFragmentComponent
    }

    interface ComponentProvider {
        fun composeStatusDialogFragmentComponent(): Factory
    }
}
