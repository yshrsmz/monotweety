package net.yslibrary.monotweety.ui.compose

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

    data class Tweet(val status: String) : ComposeTweetAction()
    object Tweeting : ComposeTweetAction()
    object Tweeted : ComposeTweetAction()
    data class TweetFailed(val error: Exception) : ComposeTweetAction()
}

sealed class ComposeTweetEffect : Effect {
    // notify view to update text input
    data class UpdateTweetString(val tweet: String) : ComposeTweetEffect()
}

data class ComposeTweetState(
    val state: ULIEState,
    val tweet: String?,
    val isTweetValid: Boolean,
    val tweetLength: Int,
    val tweetMaxLength: Int,
) : State {

    companion object {
        fun initialState(): ComposeTweetState {
            return ComposeTweetState(
                state = ULIEState.UNINITIALIZED,
                tweet = null,
                isTweetValid = false,
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
        INVALID, VALID, TWEETING, TWEETED
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
                previousState.copy(
                    state = ULIEState.IDLE,
                    tweet = action.tweet,
                    isTweetValid = action.isValid,
                    tweetLength = action.length,
                    tweetMaxLength = action.maxLength
                )
            }
            is ComposeTweetAction.Tweet -> TODO()
            ComposeTweetAction.Tweeting -> TODO()
            ComposeTweetAction.Tweeted -> TODO()
            is ComposeTweetAction.TweetFailed -> TODO()
        }
    }
}
