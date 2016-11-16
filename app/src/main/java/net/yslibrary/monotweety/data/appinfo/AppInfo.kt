package net.yslibrary.monotweety.data.appinfo

/**
 * Created by yshrsmz on 2016/11/12.
 */
data class AppInfo(val name: String,
                   val packageName: String,
                   val installed: Boolean) {
  companion object {
    fun empty(): AppInfo = AppInfo("", "", false)
  }
}