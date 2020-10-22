package net.yslibrary.monotweety.domain.status

import net.yslibrary.monotweety.data.status.StatusRepository
import javax.inject.Inject

interface UpdateStatus {
    suspend operator fun invoke(status: String)
}

internal class UpdateStatusImpl @Inject constructor(
    private val statusRepository: StatusRepository,
) : UpdateStatus {
    override suspend fun invoke(status: String) {
        statusRepository.update(status)
    }
}
