package net.yslibrary.monotweety

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by yshrsmz on 2016/09/24.
 */
@Module
class AppModule(private val context: Context) {

  @ForApp
  @AppScope
  @Provides
  fun provideAppContext(): Context = context

  @AppScope
  @Provides
  open fun provideAppLifecycleCallbacks(@ForApp context: Context): App.LifecycleCallbacks {
    return AppLifecycleCallbacks(context)
  }
}