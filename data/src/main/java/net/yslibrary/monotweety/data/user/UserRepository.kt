package net.yslibrary.monotweety.data.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userFlow: Flow<User?>
    suspend fun delete()
    suspend fun fetch()
}
