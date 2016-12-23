package net.yslibrary.monotweety

import android.content.Context

/**
 * Created by yshrsmz on 2016/09/26.
 */
class DebugAppModule(context: Context) : AppModule(context) {

  override fun provideAppLifecycleCallbacks(context: Context): App.LifecycleCallbacks {
    return DebugAppLifecycleCallbacks(context)
  }
}