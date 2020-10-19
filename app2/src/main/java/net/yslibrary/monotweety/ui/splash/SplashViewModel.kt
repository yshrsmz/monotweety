package net.yslibrary.monotweety.ui.splash

import androidx.lifecycle.ViewModel
import net.yslibrary.monotweety.base.CoroutineDispatchers
import net.yslibrary.monotweety.domain.session.ObserveSession
import net.yslibrary.monotweety.ui.arch.State
import javax.inject.Inject

sealed class SplashIntent {
    object InitialLoad : SplashIntent()
}

sealed class SplashAction {
    object CheckSession : SplashAction()
}

sealed class SplashEffect {
    // empty
}

data class SplashState(
    val hasSession: Boolean
) : State

class SplashViewModel @Inject constructor(
    private val observeSession: ObserveSession,
    dispatchers: CoroutineDispatchers,
) : ViewModel() {
}
