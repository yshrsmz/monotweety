package net.yslibrary.monotweety.splash

import dagger.Subcomponent
import net.yslibrary.monotweety.base.di.ControllerScope

@ControllerScope
@Subcomponent(
    modules = [SplashViewModule::class]
)
interface SplashComponent {
    fun inject(controller: SplashController)

    interface ComponentProvider {
        fun splashComponent(module: SplashViewModule): SplashComponent
    }
}
