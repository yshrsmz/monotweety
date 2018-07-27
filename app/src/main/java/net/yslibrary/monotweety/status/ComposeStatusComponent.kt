package net.yslibrary.monotweety.status

import dagger.Subcomponent
import net.yslibrary.monotweety.base.di.ControllerScope

@ControllerScope
@Subcomponent(
    modules = arrayOf(ComposeStatusViewModule::class)
)
interface ComposeStatusComponent {

    fun inject(controller: ComposeStatusController)

    interface ComponentProvider {
        fun composeStatusComponent(module: ComposeStatusViewModule): ComposeStatusComponent
    }
}
