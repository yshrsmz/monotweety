package net.yslibrary.monotweety.ui.login

import net.yslibrary.monotweety.base.CoroutineDispatchers
import net.yslibrary.monotweety.ui.arch.Action
import net.yslibrary.monotweety.ui.arch.Effect
import net.yslibrary.monotweety.ui.arch.Intent
import net.yslibrary.monotweety.ui.arch.MviViewModel
import net.yslibrary.monotweety.ui.arch.Processor
import net.yslibrary.monotweety.ui.arch.State
import net.yslibrary.monotweety.ui.arch.ULIEState
import javax.inject.Inject

sealed class LoginIntent : Intent {
    object Initialize : LoginIntent()
    object DoLogin : LoginIntent()
}

sealed class LoginAction : Action {
    object Initialize : LoginAction()
    object DoLogin : LoginAction()
}

sealed class LoginEffect : Effect {
    object ToMain : LoginEffect()
    object ToLoginForm : LoginEffect()
}

data class LoginState(
    val state: ULIEState,
) : State {
    companion object {
        fun initialState(): LoginState {
            return LoginState(
                state = ULIEState.UNINITIALIZED
            )
        }
    }
}

class LoginProcessor @Inject constructor(
    dispatchers: CoroutineDispatchers,
) : Processor<LoginAction>(dispatchers) {
    override fun processAction(action: LoginAction) {

    }
}

class LoginViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
) : MviViewModel<LoginIntent, LoginAction, LoginState, LoginEffect>(
    initialState = LoginState.initialState(),
    dispatchers = dispatchers
) {
    override fun intentToAction(intent: LoginIntent, state: LoginState): Action {
        return when (intent) {
            LoginIntent.Initialize -> LoginAction.Initialize
            LoginIntent.DoLogin -> LoginAction.DoLogin
        }
    }

    override fun reduce(previousState: LoginState, action: LoginAction): LoginState {
        return when (action) {
            LoginAction.Initialize -> {
                previousState.copy(state = ULIEState.IDLE)
            }
            LoginAction.DoLogin -> {
                sendEffect(LoginEffect.ToLoginForm)
                previousState
            }
        }
    }
}
