package net.yslibrary.monotweety

import android.content.Context

/**
 * Created by yshrsmz on 2016/09/24.
 */
class Modules {
  companion object {
    fun appModule(context: Context) = AppModule(context)
  }
}