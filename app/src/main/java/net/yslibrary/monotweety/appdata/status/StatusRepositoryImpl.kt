package net.yslibrary.monotweety.data.status

import io.reactivex.Completable
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.status.remote.StatusRemoteRepository
import timber.log.Timber
import javax.inject.Inject

@UserScope
class StatusRepositoryImpl @Inject constructor(
    private val remoteRepository: StatusRemoteRepository
) : StatusRepository {

    override fun updateStatus(status: String, inReplyToStatusId: Long?): Completable {
        return remoteRepository.update(status, inReplyToStatusId)
            .doOnSuccess {
                Timber.d("status updated: ${it.text}")
            }
            .ignoreElement()
    }
}
