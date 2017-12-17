package net.yslibrary.monotweety.status.domain

import com.twitter.twittertext.Validator
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.config.ConfigDataManager
import rx.Single
import javax.inject.Inject

@UserScope
class CheckStatusLength @Inject constructor(private val validator: Validator,
                                            private val configDataManager: ConfigDataManager) {

  fun execute(status: String): Single<Result> {
    return configDataManager.shortUrlLengthHttps()
        .first()
        .map {
          validator.shortUrlLength = it
          validator.shortUrlLengthHttps = it

          Result(
              status = status,
              length = validator.getTweetLength(status),
              valid = validator.isValidTweet(status))
        }
        .toSingle()
  }

  data class Result(val status: String, val length: Int, val valid: Boolean)
}