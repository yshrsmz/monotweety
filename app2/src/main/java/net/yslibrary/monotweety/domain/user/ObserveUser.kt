package net.yslibrary.monotweety.domain.user

import kotlinx.coroutines.flow.Flow
import net.yslibrary.monotweety.data.user.User

interface ObserveUser {
    fun invoke(): Flow<User>
}
