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
import net.yslibrary.monotweety.base.di.Names
import javax.inject.Named
import javax.inject.Singleton

@Module
open class AppModule {

    @Singleton
    @Provides
    open fun provideAppLifecycleCallbacks(
        context: Context,
        notificationManager: NotificationManager
    ): App.LifecycleCallbacks {
        return AppLifecycleCallbacks(context, notificationManager)
    }

    @Singleton
    @Provides
    fun provideNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Singleton
    @Provides
    fun provideNotificationManagerCompat(context: Context): NotificationManagerCompat {
        return NotificationManagerCompat.from(context)
    }

    @Singleton
    @Provides
    fun providePackageManager(context: Context): PackageManager {
        return context.packageManager
    }

    @Singleton
    @Provides
    fun provideClock(): Clock {
        return ClockImpl()
    }

    @Singleton
    @Provides
    fun provideConfig(): Config {
        return Config.init()
    }

    @Singleton
    @Provides
    open fun provideObjectWatcher(): ObjectWatcher {
        return AppWatcher.objectWatcher
    }

    @Provides
    fun provideAnalytics(context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Named(Names.FOR_LOGIN)
    @Singleton
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
