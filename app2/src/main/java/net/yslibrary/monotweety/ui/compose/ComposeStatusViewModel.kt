package net.yslibrary.monotweety.ui.compose

import kotlinx.coroutines.launch
import net.yslibrary.monotweety.base.CoroutineDispatchers
import net.yslibrary.monotweety.domain.status.BuildAndValidateStatusString
import net.yslibrary.monotweety.ui.arch.Action
import net.yslibrary.monotweety.ui.arch.Effect
import net.yslibrary.monotweety.ui.arch.Intent
import net.yslibrary.monotweety.ui.arch.MviViewModel
import net.yslibrary.monotweety.ui.arch.Processor
import net.yslibrary.monotweety.ui.arch.State
import net.yslibrary.monotweety.ui.arch.ULIEState
import javax.inject.Inject

sealed class ComposeStatusIntent : Intent {
    data class Initialize(val status: String) : ComposeStatusIntent()
    data class StatusUpdated(val status: String) : ComposeStatusIntent()
}

sealed class ComposeStatusAction : Action {
    data class Initialize(val status: String) : ComposeStatusAction()

    data class ValidateStatus(val status: String) : ComposeStatusAction()

    data class StatusValidated(
        val status: String,
        val isValid: Boolean,
        val length: Int,
        val maxLength: Int,
    ) : ComposeStatusAction()
}

sealed class ComposeStatusEffect : Effect {}

data class ComposeStatusState(
    val state: ULIEState,
    val status: String,
    val isStatusValid: Boolean,
    val statusLength: Int,
    val statusMaxLength: Int,
) : State {

    companion object {
        fun initialState(): ComposeStatusState {
            return ComposeStatusState(
                state = ULIEState.UNINITIALIZED,
                status = "",
                isStatusValid = false,
                statusLength = 0,
                statusMaxLength = 0
            )
        }
    }
}

class ComposeStatusProcessor @Inject constructor(
    private val buildAndValidateStatusString: BuildAndValidateStatusString,
    dispatchers: CoroutineDispatchers,
) : Processor<ComposeStatusAction>(
    dispatchers = dispatchers
) {
    override fun processAction(action: ComposeStatusAction) {
        when (action) {
            is ComposeStatusAction.Initialize -> {
                buildAndValidateStatus(action.status)
            }
            is ComposeStatusAction.ValidateStatus -> {
                buildAndValidateStatus(action.status)
            }
        }
    }

    private fun buildAndValidateStatus(status: String) {
        launch {
            val result = buildAndValidateStatusString(status)
            put(ComposeStatusAction.StatusValidated(
                status = result.status,
                isValid = result.isValid,
                length = result.length,
                maxLength = result.maxLength,
            ))
        }
    }
}

class ComposeStatusViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    processor: ComposeStatusProcessor,
) : MviViewModel<ComposeStatusIntent, ComposeStatusAction, ComposeStatusState, ComposeStatusEffect>(
    initialState = ComposeStatusState.initialState(),
    dispatchers = dispatchers,
    processor = processor
) {
    override fun intentToAction(intent: ComposeStatusIntent, state: ComposeStatusState): Action {
        return when (intent) {
            is ComposeStatusIntent.Initialize -> ComposeStatusAction.Initialize(intent.status)
            is ComposeStatusIntent.StatusUpdated -> ComposeStatusAction.ValidateStatus(intent.status)
        }
    }

    override fun reduce(
        previousState: ComposeStatusState,
        action: ComposeStatusAction,
    ): ComposeStatusState {
        return when (action) {
            is ComposeStatusAction.Initialize -> {
                previousState.copy(state = ULIEState.LOADING)
            }
            is ComposeStatusAction.ValidateStatus -> previousState
            is ComposeStatusAction.StatusValidated -> {
                previousState.copy(
                    state = ULIEState.IDLE,
                    status = action.status,
                    isStatusValid = action.isValid,
                    statusLength = action.length,
                    statusMaxLength = action.maxLength
                )
            }
        }
    }
}
