package net.yslibrary.monotweety

import dagger.Component
import net.yslibrary.monotweety.base.di.AppScope
import net.yslibrary.monotweety.data.DataModule
import net.yslibrary.monotweety.login.domain.IsLoggedIn
import net.yslibrary.monotweety.setting.domain.KeepDialogOpenManager
import net.yslibrary.monotweety.setting.domain.NotificationEnabledManager

/**
 * Created by yshrsmz on 2016/09/24.
 */
@AppScope
@Component(
    modules = arrayOf(AppModule::class, DataModule::class)
)
interface AppComponent : UserComponent.ComponentProvider {
  fun inject(app: App)

  fun isLoggedIn(): IsLoggedIn

  fun notificationEnabledManager(): NotificationEnabledManager

  fun keepDialogOpenManager(): KeepDialogOpenManager
}