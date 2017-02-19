package net.yslibrary.monotweety

import android.content.Context

class DebugAppModule(context: Context) : AppModule(context) {

  override fun provideAppLifecycleCallbacks(context: Context): App.LifecycleCallbacks {
    return DebugAppLifecycleCallbacks(context)
  }
}