package net.yslibrary.monotweety.data.config

import com.twitter.sdk.android.core.models.Configuration as TwitterConfig

data class Configuration(val shortUrlLengthHttps: Int) {

  companion object {
    fun from(config: TwitterConfig): Configuration {
      return Configuration(
          shortUrlLengthHttps = config.shortUrlLengthHttps
      )
    }
  }
}