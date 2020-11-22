package net.yslibrary.monotweety.domain.status

import com.twitter.twittertext.TwitterTextParser
import kotlinx.coroutines.flow.first
import net.yslibrary.monotweety.data.settings.SettingsRepository
import javax.inject.Inject

interface BuildAndValidateStatusString {
    suspend operator fun invoke(status: String): Result

    data class Result(
        val status: String,
        val length: Int,
        val isValid: Boolean,
        val maxLength: Int,
    )
}

internal class BuildAndValidateStatusStringImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : BuildAndValidateStatusString {
    override suspend fun invoke(status: String): BuildAndValidateStatusString.Result {
        val settings = settingsRepository.settingsFlow.first()

        val finalStatus = when {
            settings.footerEnabled -> "$status ${settings.footerText}"
            else -> status
        }.trim()

        val result = TwitterTextParser.parseTweet(
            finalStatus,
            TwitterTextParser.TWITTER_TEXT_DEFAULT_CONFIG)

        return BuildAndValidateStatusString.Result(
            status = finalStatus,
            length = result.weightedLength,
            isValid = result.isValid,
            maxLength = TwitterTextParser.TWITTER_TEXT_DEFAULT_CONFIG.maxWeightedTweetLength
        )
    }
}
