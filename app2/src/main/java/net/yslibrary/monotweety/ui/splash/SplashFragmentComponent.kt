package net.yslibrary.monotweety.ui.splash

import dagger.Module
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
        fun splashFragmentComponent(): SplashFragmentComponent.Factory
    }
}

@Module(
    subcomponents = [
        SplashFragmentComponent::class,
    ]
)
interface SplashFragmentModule
