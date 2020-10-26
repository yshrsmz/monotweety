package net.yslibrary.monotweety.domain.user

import net.yslibrary.monotweety.data.user.UserRepository
import javax.inject.Inject

interface FetchUser {
    suspend operator fun invoke()
}

internal class FetchUserImpl @Inject constructor(
    private val userRepository: UserRepository,
) : FetchUser {
    override suspend fun invoke() {
        userRepository.fetch()
    }
}
