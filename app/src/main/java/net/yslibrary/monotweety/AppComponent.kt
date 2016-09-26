package net.yslibrary.monotweety

import dagger.Component
import net.yslibrary.monotweety.base.di.AppScope

/**
 * Created by yshrsmz on 2016/09/24.
 */
@AppScope
@Component(
    modules = arrayOf(AppModule::class)
)
interface AppComponent : UserComponent.ComponentProvider {
  fun inject(app: App)
}