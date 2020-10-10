package net.yslibrary.monotweety

import android.content.Context
import com.twitter.sdk.android.core.TwitterSession
import dagger.BindsInstance
import dagger.Component
import io.reactivex.subjects.PublishSubject
import net.yslibrary.monotweety.analytics.Analytics
import net.yslibrary.monotweety.appdata.DataModule
import net.yslibrary.monotweety.base.ObjectWatcherDelegate
import net.yslibrary.monotweety.base.di.Names
import net.yslibrary.monotweety.login.domain.IsLoggedIn
import net.yslibrary.monotweety.setting.domain.FooterStateManager
import net.yslibrary.monotweety.setting.domain.NotificationEnabledManager
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, DataModule::class]
)
interface AppComponent : UserComponent.ComponentProvider {
    fun inject(app: App)

    fun isLoggedIn(): IsLoggedIn

    fun notificationEnabledManager(): NotificationEnabledManager

    fun footerStateManager(): FooterStateManager

    fun objectWatcherDelegate(): ObjectWatcherDelegate

    fun analytics(): Analytics

    @Named(Names.FOR_LOGIN)
    fun loginCompletedSubject(): PublishSubject<TwitterSession>

    @Component.Factory
    interface Factory {
        fun create(
            appModule: AppModule,
            @BindsInstance context: Context,
        ): AppComponent
    }
}
