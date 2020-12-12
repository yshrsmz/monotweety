package net.yslibrary.monotweety.ui.loginform

import com.codingfeline.twitter4kt.core.model.oauth1a.AccessToken
import com.codingfeline.twitter4kt.core.model.oauth1a.RequestToken
import com.codingfeline.twitter4kt.core.oauth1a.authorizationUrl
import kotlinx.coroutines.launch
import net.yslibrary.monotweety.base.CoroutineDispatchers
import net.yslibrary.monotweety.data.auth.AuthFlow
import net.yslibrary.monotweety.ui.arch.Action
import net.yslibrary.monotweety.ui.arch.Effect
import net.yslibrary.monotweety.ui.arch.GlobalAction
import net.yslibrary.monotweety.ui.arch.Intent
import net.yslibrary.monotweety.ui.arch.MviViewModel
import net.yslibrary.monotweety.ui.arch.Processor
import net.yslibrary.monotweety.ui.arch.State
import net.yslibrary.monotweety.ui.arch.ULIEState
import timber.log.Timber
import javax.inject.Inject

sealed class LoginFormIntent : Intent {
    object Initialize : LoginFormIntent()
    object OpenBrowser : LoginFormIntent()
    data class PinCodeUpdated(val value: String) : LoginFormIntent()
    object Authorize : LoginFormIntent()
}

sealed class LoginFormAction : Action {
    object Initialize : LoginFormAction()
    object LoadRequestToken : LoginFormAction()

    data class RequestTokenError(val error: Throwable) : LoginFormAction()
    data class RequestTokenLoaded(val token: RequestToken) : LoginFormAction()

    data class Authorize(
        val requestToken: RequestToken,
        val pinCode: String,
    ) : LoginFormAction()

    data class AccessTokenError(val error: Throwable) : LoginFormAction()
    data class AccessTokenLoaded(val token: AccessToken) : LoginFormAction()
    data class PinCodeUpdated(val value: String) : LoginFormAction()
}

sealed class LoginFormEffect : Effect {
    data class OpenBrowser(val url: String) : LoginFormEffect()
    object ToMain : LoginFormEffect()

    data class ShowError(val message: String?) : LoginFormEffect()
}

/**
 * +------+
 * | Idle |
 * +------+
 *    |
 *    | tap Open Browser
 *    V
 * +---------------------+               +-----------------------+
 * | LoadingRequestToken | ------------> | LoadRequestTokenError |
 * +---------------------+ <------------ +-----------------------+
 *    |                 A A
 *    | go to Twitter   | |
 *    V                 | +-------------------+
 * +----------------+   | re-request          |
 * | WaitForPinCode | --+                     |
 * +----------------+                         | re-request
 *    |                                       |
 *    | pincode entered                       |
 *    V                                       |
 * +-------------+                         +----------------------+
 * | Authorizing | ----------------------> | LoadAccessTokenError |
 * +-------------+ <---------------------- +----------------------+
 *    |
 *    | AccessToken received
 *    V
 * +----------+
 * | Finished |
 * +----------+
 */
enum class LoginFlowState {
    Idle,
    LoadingRequestToken,
    LoadRequestTokenError,
    WaitForPinCode,
    Authorizing,
    LoadAccessTokenError,
    Finished;

    fun isAtLeast(state: LoginFlowState): Boolean = compareTo(state) >= 0
    fun isAtMost(state: LoginFlowState): Boolean = compareTo(state) <= 0
}

data class LoginFormState(
    val state: ULIEState,
    val loginFlowState: LoginFlowState,
    val requestToken: RequestToken?,
    val accessToken: AccessToken?,
    val pinCode: String,
) : State {

    val pinCodeIsValid: Boolean by lazy { isValidPinCode(pinCode) }

    companion object {
        fun initialState(): LoginFormState {
            return LoginFormState(
                state = ULIEState.UNINITIALIZED,
                loginFlowState = LoginFlowState.Idle,
                requestToken = null,
                accessToken = null,
                pinCode = "",
            )
        }
    }
}

class LoginFormProcessor @Inject constructor(
    private val authFlow: AuthFlow,
    dispatchers: CoroutineDispatchers,
) : Processor<LoginFormAction>(
    dispatchers = dispatchers,
) {
    override fun processAction(action: LoginFormAction) {
        when (action) {
            LoginFormAction.LoadRequestToken -> {
                loadRequestToken()
            }
            is LoginFormAction.Authorize -> {
                loadAccessToken(action.requestToken, action.pinCode)
            }
            LoginFormAction.Initialize,
            is LoginFormAction.RequestTokenLoaded,
            is LoginFormAction.AccessTokenLoaded,
            is LoginFormAction.RequestTokenError,
            is LoginFormAction.AccessTokenError,
            is LoginFormAction.PinCodeUpdated,
            -> {
                // no-op
            }
        }
    }

    private fun loadRequestToken() {
        launch {
            try {
                val token = authFlow.getRequestToken()
                put(LoginFormAction.RequestTokenLoaded(token))
            } catch (e: Throwable) {
                Timber.e(e, "load request token error: $e.")
                put(LoginFormAction.RequestTokenError(e))
            }
        }
    }

    private fun loadAccessToken(requestToken: RequestToken, pinCode: String) {
        launch {
            try {
                val token = authFlow.getAccessToken(requestToken, pinCode)
                put(LoginFormAction.AccessTokenLoaded(token))
            } catch (e: Throwable) {
                Timber.e(e, "load access token error: $e.")
                put(LoginFormAction.AccessTokenError(e))
            }
        }
    }
}

class LoginFormViewModel @Inject constructor(
    processor: LoginFormProcessor,
    dispatchers: CoroutineDispatchers,
) : MviViewModel<LoginFormIntent, LoginFormAction, LoginFormState, LoginFormEffect>(
    initialState = LoginFormState.initialState(),
    processor = processor,
    dispatchers = dispatchers,
) {
    override fun intentToAction(intent: LoginFormIntent, state: LoginFormState): Action {
        return when (intent) {
            LoginFormIntent.Initialize -> LoginFormAction.Initialize
            LoginFormIntent.OpenBrowser -> {
                when (state.loginFlowState) {
                    LoginFlowState.Idle -> LoginFormAction.LoadRequestToken
                    LoginFlowState.LoadingRequestToken -> GlobalAction.NoOp
                    LoginFlowState.WaitForPinCode -> LoginFormAction.LoadRequestToken
                    LoginFlowState.Authorizing -> GlobalAction.NoOp
                    LoginFlowState.LoadRequestTokenError -> LoginFormAction.LoadRequestToken
                    LoginFlowState.LoadAccessTokenError -> LoginFormAction.LoadRequestToken
                    LoginFlowState.Finished -> GlobalAction.NoOp
                }
            }
            is LoginFormIntent.Authorize -> {
                if (state.pinCodeIsValid && state.loginFlowState == LoginFlowState.WaitForPinCode) {
                    LoginFormAction.Authorize(state.requestToken!!, state.pinCode)
                } else GlobalAction.NoOp
            }
            is LoginFormIntent.PinCodeUpdated -> LoginFormAction.PinCodeUpdated(intent.value)
        }
    }

    override fun reduce(previousState: LoginFormState, action: LoginFormAction): LoginFormState {
        return when (action) {
            LoginFormAction.Initialize -> {
                previousState.copy(
                    state = ULIEState.IDLE,
                    loginFlowState = LoginFlowState.Idle,
                    requestToken = null,
                    accessToken = null,
                )
            }
            LoginFormAction.LoadRequestToken -> {
                previousState.copy(
                    state = ULIEState.IDLE,
                    loginFlowState = LoginFlowState.LoadingRequestToken,
                    requestToken = null,
                    accessToken = null,
                )
            }
            is LoginFormAction.RequestTokenLoaded -> {
                sendEffect(LoginFormEffect.OpenBrowser(url = action.token.authorizationUrl()))
                previousState.copy(
                    state = ULIEState.IDLE,
                    loginFlowState = LoginFlowState.WaitForPinCode,
                    requestToken = action.token,
                )
            }
            is LoginFormAction.RequestTokenError -> {
                sendEffect(LoginFormEffect.ShowError(action.error.message))
                previousState.copy(
                    state = ULIEState.ERROR(action.error),
                    loginFlowState = LoginFlowState.LoadRequestTokenError
                )
            }
            is LoginFormAction.PinCodeUpdated -> {
                previousState.copy(
                    state = ULIEState.IDLE,
                    pinCode = action.value
                )
            }
            is LoginFormAction.Authorize -> {
                previousState.copy(
                    state = ULIEState.IDLE,
                    loginFlowState = LoginFlowState.Authorizing,
                    accessToken = null,
                )
            }
            is LoginFormAction.AccessTokenLoaded -> {
                sendEffect(LoginFormEffect.ToMain)
                previousState.copy(
                    state = ULIEState.IDLE,
                    loginFlowState = LoginFlowState.Finished,
                    accessToken = action.token,
                )
            }
            is LoginFormAction.AccessTokenError -> {
                sendEffect(LoginFormEffect.ShowError(action.error.message))
                previousState.copy(
                    state = ULIEState.ERROR(action.error),
                    loginFlowState = LoginFlowState.LoadAccessTokenError
                )
            }
        }
    }
}

private fun isValidPinCode(value: String): Boolean {
    return value.toIntOrNull(10) != null
}
