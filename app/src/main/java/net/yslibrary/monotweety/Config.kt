package net.yslibrary.monotweety

data class Config(val developerUrl: String,
                  val googlePlayUrl: String,
                  val githubUrl: String,
                  val statusMaxLength: Int = 140) {
    companion object {
        fun init(): Config {
            return Config(
                developerUrl = "https://twitter.com/yslibnet",
                googlePlayUrl = "https://play.google.com/store/apps/details?id=net.yslibrary.monotweety",
                githubUrl = "https://github.com/yshrsmz/monotweety"
            )
        }
    }
}
