package net.yslibrary.monotweety.ui.compose

import dagger.Subcomponent
import net.yslibrary.monotweety.di.FragmentScope

@FragmentScope
@Subcomponent
interface ComposeTweetDialogFragmentComponent {
    fun inject(fragment: ComposeTweetDialogFragment)

    @Subcomponent.Factory
    interface Factory {
        fun build(): ComposeTweetDialogFragmentComponent
    }

    interface ComponentProvider {
        fun composeTweetDialogFragmentComponent(): Factory
    }
}
