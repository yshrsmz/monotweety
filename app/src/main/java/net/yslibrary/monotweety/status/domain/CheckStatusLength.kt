package net.yslibrary.monotweety.status.domain

import com.twitter.twittertext.TwitterTextParser
import io.reactivex.Single
import net.yslibrary.monotweety.base.di.UserScope
import javax.inject.Inject

@UserScope
class CheckStatusLength @Inject constructor() {

    fun execute(status: String): Single<Result> {
        return Single.fromCallable {
            val result = TwitterTextParser.parseTweet(status, TwitterTextParser.TWITTER_TEXT_EMOJI_CHAR_COUNT_CONFIG)
            Result(
                status = status,
                length = result.weightedLength,
                valid = result.isValid,
                maxLength = TwitterTextParser.TWITTER_TEXT_EMOJI_CHAR_COUNT_CONFIG.maxWeightedTweetLength)
        }
    }

    data class Result(val status: String,
                      val length: Int,
                      val valid: Boolean,
                      val maxLength: Int)
}
