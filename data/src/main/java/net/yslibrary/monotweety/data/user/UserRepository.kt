package net.yslibrary.monotweety.data.user

import com.codingfeline.twitter4kt.core.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import net.yslibrary.monotweety.data.user.local.UserLocalGateway
import net.yslibrary.monotweety.data.user.remote.UserRemoteGateway
import net.yslibrary.monotweety.di.UserScope
import javax.inject.Inject

interface UserRepository {
    val userFlow: Flow<User?>
    suspend fun delete()
    suspend fun fetch()
}

@UserScope
internal class UserRepositoryImpl @Inject constructor(
    private val remoteGateway: UserRemoteGateway,
    private val localGateway: UserLocalGateway,
    private val clock: Clock,
) : UserRepository {
    override val userFlow: Flow<User?> = localGateway.userFlow

    override suspend fun delete() {
        localGateway.delete()
    }

    override suspend fun fetch() {
        val result = remoteGateway.verifyCredentials()
        if (result is ApiResult.Success) {
            val account = result.value
            localGateway.update(
                User(
                    id = account.idStr,
                    name = account.name,
                    screenName = account.screenName,
                    profileImageUrl = account.profileImageUrlHttps,
                    updatedAt = clock.now().epochSeconds
                )
            )
        }
    }
}
