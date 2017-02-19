package net.yslibrary.monotweety

import android.content.Context

class Modules {
  companion object {
    fun appModule(context: Context) = DebugAppModule(context)
  }
}