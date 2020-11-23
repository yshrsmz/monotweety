package net.yslibrary.monotweety.ui.splash

import dagger.Subcomponent
import net.yslibrary.monotweety.di.FragmentScope


@FragmentScope
@Subcomponent
interface SplashFragmentComponent {
    fun inject(fragment: SplashFragment)

    @Subcomponent.Factory
    interface Factory {
        fun build(): SplashFragmentComponent
    }

    interface ComponentProvider {
        fun splashFragmentComponent(): Factory
    }
}

