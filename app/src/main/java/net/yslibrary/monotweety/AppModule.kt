package net.yslibrary.monotweety

import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.twitter.sdk.android.core.TwitterSession
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import leakcanary.AppWatcher
import leakcanary.ObjectWatcher
import net.yslibrary.monotweety.base.Clock
import net.yslibrary.monotweety.base.ClockImpl
import net.yslibrary.monotweety.base.ObjectWatcherDelegate
import net.yslibrary.monotweety.base.ObjectWatcherDelegateImpl
import net.yslibrary.monotweety.base.di.AppScope
import net.yslibrary.monotweety.base.di.Names
import javax.inject.Named

@Module
open class AppModule(private val context: Context) {

    @Named(Names.FOR_APP)
    @AppScope
    @Provides
    fun provideAppContext(): Context = context

    @AppScope
    @Provides
    open fun provideAppLifecycleCallbacks(
        @Named(Names.FOR_APP) context: Context,
        notificationManager: NotificationManager
    ): App.LifecycleCallbacks {
        return AppLifecycleCallbacks(context, notificationManager)
    }

    @AppScope
    @Provides
    fun provideNotificationManager(@Named(Names.FOR_APP) context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @AppScope
    @Provides
    fun provideNotificationManagerCompat(@Named(Names.FOR_APP) context: Context): NotificationManagerCompat {
        return NotificationManagerCompat.from(context)
    }

    @AppScope
    @Provides
    fun providePackageManager(@Named(Names.FOR_APP) context: Context): PackageManager {
        return context.packageManager
    }

    @AppScope
    @Provides
    fun provideClock(): Clock {
        return ClockImpl()
    }

    @AppScope
    @Provides
    fun provideConfig(): Config {
        return Config.init()
    }

    @AppScope
    @Provides
    open fun provideObjectWatcher(): ObjectWatcher {
        return AppWatcher.objectWatcher
    }

    @Provides
    fun provideAnalytics(@Named(Names.FOR_APP) context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Named(Names.FOR_LOGIN)
    @AppScope
    @Provides
    fun provideLoginCompletedSubject(): PublishSubject<TwitterSession> {
        return PublishSubject.create()
    }

    @Provides
    fun provideRefWatcherDelegate(refWatcherDelegateImpl: ObjectWatcherDelegateImpl): ObjectWatcherDelegate {
        return refWatcherDelegateImpl
    }

    interface Provider {
        fun notificationManager(): NotificationManagerCompat
        fun clock(): Clock
        fun config(): Config
        fun refWatcherDelegate(): ObjectWatcherDelegate
        @Named(Names.FOR_LOGIN)
        fun loginCompletedSubject(): PublishSubject<TwitterSession>
    }
}
