package net.yslibrary.monotweety.domain.user

import kotlinx.coroutines.flow.Flow
import net.yslibrary.monotweety.data.user.User
import net.yslibrary.monotweety.data.user.UserRepository
import javax.inject.Inject

interface ObserveUser {
    operator fun invoke(): Flow<User?>
}

internal class ObserveUserImpl @Inject constructor(
    private val userRepository: UserRepository,
) : ObserveUser {
    override fun invoke(): Flow<User?> {
        return userRepository.userFlow
    }
}
