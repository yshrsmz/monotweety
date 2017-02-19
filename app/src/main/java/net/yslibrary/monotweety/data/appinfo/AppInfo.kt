package net.yslibrary.monotweety.data.appinfo

data class AppInfo(val name: String,
                   val packageName: String,
                   val installed: Boolean) {
  companion object {
    fun empty(): AppInfo = AppInfo("", "", false)
  }
}