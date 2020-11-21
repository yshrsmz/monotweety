package net.yslibrary.monotweety.data.status

import com.codingfeline.twitter4kt.core.ApiResult
import com.codingfeline.twitter4kt.v1.api.statuses.StatusesApi
import com.codingfeline.twitter4kt.v1.api.statuses.update
import com.codingfeline.twitter4kt.v1.model.status.Tweet
import net.yslibrary.monotweety.di.UserScope
import javax.inject.Inject

interface StatusRepository {
    suspend fun update(status: String): ApiResult<Tweet>
}

@UserScope
internal class StatusRepositoryImpl @Inject constructor(
    private val statusesApi: StatusesApi,
) : StatusRepository {
    override suspend fun update(status: String): ApiResult<Tweet> {
        return statusesApi.update(status)
    }
}
