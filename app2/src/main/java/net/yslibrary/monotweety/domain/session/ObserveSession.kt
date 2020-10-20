package net.yslibrary.monotweety.domain.session

import kotlinx.coroutines.flow.Flow
import net.yslibrary.monotweety.data.session.Session
import net.yslibrary.monotweety.data.session.SessionRepository
import javax.inject.Inject

interface ObserveSession {
    operator fun invoke(): Flow<Session?>
}

internal class ObserveSessionImpl @Inject constructor(
    private val sessionRepository: SessionRepository
) : ObserveSession {
    override fun invoke(): Flow<Session?> {
        return sessionRepository.sessionFlow
    }
}
