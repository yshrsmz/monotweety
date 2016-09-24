package net.yslibrary.monotweety

import dagger.Component

/**
 * Created by yshrsmz on 2016/09/24.
 */
@AppScope
@Component(
    modules = arrayOf(AppModule::class)
)
interface AppComponent {
  fun inject(app: App)
}