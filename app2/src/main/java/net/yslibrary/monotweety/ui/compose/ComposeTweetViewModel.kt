package net.yslibrary.monotweety.ui.compose

import com.codingfeline.twitter4kt.core.isSuccess
import kotlinx.coroutines.launch
import net.yslibrary.monotweety.base.CoroutineDispatchers
import net.yslibrary.monotweety.domain.status.BuildAndValidateStatusString
import net.yslibrary.monotweety.domain.status.UpdateStatus
import net.yslibrary.monotweety.ui.arch.Action
import net.yslibrary.monotweety.ui.arch.Effect
import net.yslibrary.monotweety.ui.arch.GlobalAction
import net.yslibrary.monotweety.ui.arch.Intent
import net.yslibrary.monotweety.ui.arch.MviViewModel
import net.yslibrary.monotweety.ui.arch.Processor
import net.yslibrary.monotweety.ui.arch.State
import net.yslibrary.monotweety.ui.arch.ULIEState
import javax.inject.Inject

sealed class ComposeTweetIntent : Intent {
    data class Initialize(val tweet: String) : ComposeTweetIntent()
    data class TweetUpdated(val tweet: String) : ComposeTweetIntent()

    object Tweet : ComposeTweetIntent()
}

sealed class ComposeTweetAction : Action {
    data class Initialize(val tweet: String) : ComposeTweetAction()

    data class ValidateTweet(val tweet: String) : ComposeTweetAction()

    data class TweetValidated(
        val tweet: String,
        val isValid: Boolean,
        val length: Int,
        val maxLength: Int,
    ) : ComposeTweetAction()

    data class Tweet(val tweet: String) : ComposeTweetAction()
    object Tweeting : ComposeTweetAction()
    object Tweeted : ComposeTweetAction()
    data class TweetFailed(val error: Throwable) : ComposeTweetAction()
}

sealed class ComposeTweetEffect : Effect {
    // notify view to update text input
    data class UpdateTweetString(val tweet: String) : ComposeTweetEffect()

    object Dismiss : ComposeTweetEffect()
}

data class ComposeTweetState(
    val state: ULIEState,
    val tweet: String?,
    val tweetStatus: TweetStatus,
    val tweetLength: Int,
    val tweetMaxLength: Int,
) : State {

    companion object {
        fun initialState(): ComposeTweetState {
            return ComposeTweetState(
                state = ULIEState.UNINITIALIZED,
                tweet = null,
                tweetStatus = TweetStatus.INVALID,
                tweetLength = 0,
                tweetMaxLength = 0
            )
        }
    }

    /**
     * INVALID -> VALID -> TWEETING -> TWEETED
     *    A         |        |
     *    |         |        |
     *    +---------+--------+
     */
    enum class TweetStatus {
        INVALID, VALID, TWEETING, TWEETED;

        val isValid: Boolean get() = this == VALID

        val isTweeting: Boolean get() = this === TWEETING
    }
}

class ComposeTweetProcessor @Inject constructor(
    private val buildAndValidateStatusString: BuildAndValidateStatusString,
    private val updateStatus: UpdateStatus,
    dispatchers: CoroutineDispatchers,
) : Processor<ComposeTweetAction>(
    dispatchers = dispatchers
) {
    override fun processAction(action: ComposeTweetAction) {
        when (action) {
            is ComposeTweetAction.Initialize -> {
                buildAndValidateStatus(action.tweet)
            }
            is ComposeTweetAction.ValidateTweet -> {
                buildAndValidateStatus(action.tweet)
            }
            is ComposeTweetAction.Tweet -> {
                tweet(action.tweet)
            }
        }
    }

    private fun buildAndValidateStatus(status: String) {
        launch {
            val result = buildAndValidateStatusString(status)
            put(ComposeTweetAction.TweetValidated(
                tweet = result.status,
                isValid = result.isValid,
                length = result.length,
                maxLength = result.maxLength,
            ))
        }
    }

    private fun tweet(tweet: String) {
        launch {
            val result = updateStatus(tweet)
            if (result.isSuccess()) {
                put(ComposeTweetAction.Tweeted)
            } else {
                put(ComposeTweetAction.TweetFailed(result.error))
            }
        }
    }
}

class ComposeTweetViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    processor: ComposeTweetProcessor,
) : MviViewModel<ComposeTweetIntent, ComposeTweetAction, ComposeTweetState, ComposeTweetEffect>(
    initialState = ComposeTweetState.initialState(),
    dispatchers = dispatchers,
    processor = processor
) {
    override fun intentToAction(intent: ComposeTweetIntent, state: ComposeTweetState): Action {
        return when (intent) {
            is ComposeTweetIntent.Initialize -> ComposeTweetAction.Initialize(intent.tweet)
            is ComposeTweetIntent.TweetUpdated -> ComposeTweetAction.ValidateTweet(intent.tweet)
            ComposeTweetIntent.Tweet -> {
                if (state.tweet != null) ComposeTweetAction.Tweet(state.tweet) else GlobalAction.NoOp
            }
        }
    }

    override fun reduce(
        previousState: ComposeTweetState,
        action: ComposeTweetAction,
    ): ComposeTweetState {
        return when (action) {
            is ComposeTweetAction.Initialize -> {
                previousState.copy(state = ULIEState.LOADING)
            }
            is ComposeTweetAction.ValidateTweet -> previousState
            is ComposeTweetAction.TweetValidated -> {
                if (previousState.tweet == null) {
                    // should update EditText once
                    sendEffect(ComposeTweetEffect.UpdateTweetString(action.tweet))
                }
                val tweetStatus = if (action.isValid) {
                    ComposeTweetState.TweetStatus.VALID
                } else {
                    ComposeTweetState.TweetStatus.INVALID
                }

                previousState.copy(
                    state = ULIEState.IDLE,
                    tweet = action.tweet,
                    tweetStatus = tweetStatus,
                    tweetLength = action.length,
                    tweetMaxLength = action.maxLength
                )
            }
            is ComposeTweetAction.Tweet -> previousState
            ComposeTweetAction.Tweeting -> {
                previousState.copy(tweetStatus = ComposeTweetState.TweetStatus.TWEETING)
            }
            ComposeTweetAction.Tweeted -> {
                sendEffect(ComposeTweetEffect.Dismiss)
                previousState.copy(tweetStatus = ComposeTweetState.TweetStatus.TWEETED)
            }
            is ComposeTweetAction.TweetFailed -> {
                previousState.copy(tweetStatus = ComposeTweetState.TweetStatus.INVALID)
            }
        }
    }
}
