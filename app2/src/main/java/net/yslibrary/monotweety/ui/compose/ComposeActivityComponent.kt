package net.yslibrary.monotweety.ui.compose

import android.app.Activity
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import net.yslibrary.monotweety.di.ActivityScope

@ActivityScope
@Subcomponent(
    modules = [
        ComposeActivitySubcomponentModule::class,
    ]
)
interface ComposeActivityComponent : ComposeActivitySubcomponentModule.ComponentProviders {
    fun inject(activity: ComposeActivity)

    @Subcomponent.Factory
    interface Factory {
        fun build(@BindsInstance activity: Activity): ComposeActivityComponent
    }

    interface ComponentProvider {
        fun composeActivityComponent(): Factory
    }
}

@Module(
    subcomponents = [
        ComposeStatusDialogFragmentComponent::class,
    ]
)
interface ComposeActivitySubcomponentModule {
    interface ComponentProviders : ComposeStatusDialogFragmentComponent.ComponentProvider
}
