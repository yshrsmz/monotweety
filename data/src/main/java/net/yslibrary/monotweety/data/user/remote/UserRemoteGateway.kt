package net.yslibrary.monotweety.data.user.remote

import com.codingfeline.twitter4kt.core.ApiResult
import com.codingfeline.twitter4kt.v1.api.account.AccountApi
import com.codingfeline.twitter4kt.v1.api.account.verifyCredentials
import com.codingfeline.twitter4kt.v1.model.account.Account
import javax.inject.Inject

interface UserRemoteGateway {
    suspend fun verifyCredentials(): ApiResult<Account>
}

internal class UserRemoteGatewayImpl @Inject constructor(
    private val accountApi: AccountApi
) : UserRemoteGateway {
    override suspend fun verifyCredentials(): ApiResult<Account> {
        return accountApi.verifyCredentials(
            includeEntities = false,
            skipStatus = true,
            includeEmail = false
        )
    }
}
