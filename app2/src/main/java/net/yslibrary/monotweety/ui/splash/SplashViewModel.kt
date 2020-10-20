package net.yslibrary.monotweety.ui.splash

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.yslibrary.monotweety.base.CoroutineDispatchers
import net.yslibrary.monotweety.data.session.Session
import net.yslibrary.monotweety.domain.session.ObserveSession
import net.yslibrary.monotweety.ui.arch.Action
import net.yslibrary.monotweety.ui.arch.Effect
import net.yslibrary.monotweety.ui.arch.Intent
import net.yslibrary.monotweety.ui.arch.MviViewModel
import net.yslibrary.monotweety.ui.arch.Processor
import net.yslibrary.monotweety.ui.arch.State
import javax.inject.Inject

sealed class SplashIntent : Intent {
    object InitialLoad : SplashIntent()
}

sealed class SplashAction : Action {
    object CheckSession : SplashAction()
    data class SessionUpdated(
        val session: Session?,
    ) : SplashAction()
}

sealed class SplashEffect : Effect {
    object ToLogin : SplashEffect()
    object ToMain : SplashEffect()
}

data class SplashState(
    val hasSession: Boolean,
) : State {
    companion object {
        fun initialState(): SplashState {
            return SplashState(
                hasSession = false
            )
        }
    }
}

class SplashProcessor @Inject constructor(
    private val observesSession: ObserveSession,
    dispatchers: CoroutineDispatchers,
) : Processor<SplashAction>(
    dispatchers = dispatchers,
) {
    override fun processAction(action: SplashAction) {
        when (action) {
            SplashAction.CheckSession -> {
                doObserveSession()
            }
            is SplashAction.SessionUpdated -> {
                // no-op
            }
        }
    }

    private fun doObserveSession() = launch {
        val session = observesSession().first()
        put(SplashAction.SessionUpdated(session))
    }
}

class SplashViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    processor: SplashProcessor,
) : MviViewModel<SplashIntent, SplashAction, SplashState, SplashEffect>(
    initialState = SplashState.initialState(),
    dispatchers = dispatchers,
    processor = processor,
) {

    override fun intentToAction(intent: SplashIntent, state: SplashState): Action {
        return when (intent) {
            SplashIntent.InitialLoad -> SplashAction.CheckSession
        }
    }

    override fun reduce(previousState: SplashState, newAction: SplashAction): SplashState {
        return when (newAction) {
            SplashAction.CheckSession -> previousState
            is SplashAction.SessionUpdated -> {
                val hasSession = newAction.session != null
                val effect = if (hasSession) SplashEffect.ToMain else SplashEffect.ToLogin
                sendEffect(effect)
                previousState.copy(hasSession = hasSession)
            }
        }
    }


}
