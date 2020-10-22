package net.yslibrary.monotweety.data.status

import com.codingfeline.twitter4kt.core.getOrThrow
import com.codingfeline.twitter4kt.v1.api.statuses.StatusesApi
import com.codingfeline.twitter4kt.v1.api.statuses.update
import net.yslibrary.monotweety.di.UserScope
import javax.inject.Inject

interface StatusRepository {
    suspend fun update(status: String)
}

@UserScope
internal class StatusRepositoryImpl @Inject constructor(
    private val statusesApi: StatusesApi,
) : StatusRepository {
    override suspend fun update(status: String) {
        statusesApi.update(status).getOrThrow()
    }
}
