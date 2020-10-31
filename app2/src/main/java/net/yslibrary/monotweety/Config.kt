package net.yslibrary.monotweety

data class Config(
    val twitterUrl: String,
    val googlePlayUrl: String,
    val githubUrl: String,
    val privacyPolicyUrl: String,
    val statusMaxLength: Int = 280,
) {
    companion object {
        fun init(): Config {
            return Config(
                twitterUrl = "https://twitter.com/yslibnet",
                googlePlayUrl = "https://play.google.com/store/apps/details?id=net.yslibrary.monotweety",
                githubUrl = "https://github.com/yshrsmz/monotweety",
                privacyPolicyUrl = "https://www.yslibrary.net/products/monotweety/privacy-policy/",
            )
        }
    }
}
