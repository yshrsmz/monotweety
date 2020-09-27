package net.yslibrary.monotweety.data.user

import kotlinx.coroutines.flow.Flow
import net.yslibrary.monotweety.data.user.local.UserLocalGateway
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserRepositoryImpl @Inject constructor(
    private val localGateway: UserLocalGateway
) : UserRepository {
    override val userFlow: Flow<User?> = localGateway.userFlow

    override suspend fun update(user: User) {
        localGateway.update(user)
    }

    override suspend fun delete() {
        localGateway.delete()
    }

    override suspend fun fetch() {
        TODO("Not yet implemented")
    }
}

