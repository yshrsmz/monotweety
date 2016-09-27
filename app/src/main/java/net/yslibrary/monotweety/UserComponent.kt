package net.yslibrary.monotweety

import dagger.Subcomponent
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.UserDataModule

/**
 * Created by yshrsmz on 2016/09/24.
 */
@UserScope
@Subcomponent(
    modules = arrayOf(UserDataModule::class)
)
interface UserComponent : AppModule.Provider {

  interface ComponentProvider {
    fun userComponent(): UserComponent
  }
}