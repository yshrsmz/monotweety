package net.yslibrary.monotweety.status

import dagger.Subcomponent
import net.yslibrary.monotweety.base.di.ControllerScope

/**
 * Created by yshrsmz on 2016/10/02.
 */
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