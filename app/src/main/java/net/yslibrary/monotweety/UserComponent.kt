package net.yslibrary.monotweety

import dagger.Subcomponent
import net.yslibrary.monotweety.base.di.UserScope

/**
 * Created by yshrsmz on 2016/09/24.
 */
@UserScope
@Subcomponent
interface UserComponent {

  interface ComponentProvider {
    fun userComponent(): UserComponent
  }
}