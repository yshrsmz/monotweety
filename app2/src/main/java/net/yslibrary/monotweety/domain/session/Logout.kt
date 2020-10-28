package net.yslibrary.monotweety.domain.session

import net.yslibrary.monotweety.data.session.SessionRepository
import net.yslibrary.monotweety.data.user.UserRepository
import javax.inject.Inject

interface Logout {
    suspend operator fun invoke()
}

internal class LogoutImpl @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
) : Logout {
    override suspend fun invoke() {
        sessionRepository.delete()
        userRepository.delete()
    }
}
