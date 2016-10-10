package net.yslibrary.monotweety

/**
 * Created by yshrsmz on 2016/10/10.
 */
data class Config(val developerUrl: String,
                  val googlePlayUrl: String) {
  companion object {
    fun init(): Config {
      return Config(
          developerUrl = "https://twitter.com/yslibnet",
          googlePlayUrl = "https://play.google.com/store/apps/details?id=net.yslibrary.monotweety"
      )
    }
  }
}