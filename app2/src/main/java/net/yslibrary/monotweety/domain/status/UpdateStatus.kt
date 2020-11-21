package net.yslibrary.monotweety.domain.status

import com.codingfeline.twitter4kt.core.ApiResult
import com.codingfeline.twitter4kt.v1.model.status.Tweet
import net.yslibrary.monotweety.data.status.StatusRepository
import javax.inject.Inject

interface UpdateStatus {
    suspend operator fun invoke(status: String): ApiResult<Tweet>
}

internal class UpdateStatusImpl @Inject constructor(
    private val statusRepository: StatusRepository,
) : UpdateStatus {
    override suspend fun invoke(status: String): ApiResult<Tweet> {
        return statusRepository.update(status)
    }
}
