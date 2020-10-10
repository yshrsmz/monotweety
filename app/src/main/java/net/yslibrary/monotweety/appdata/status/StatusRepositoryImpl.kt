package net.yslibrary.monotweety.appdata.status

import io.reactivex.Completable
import net.yslibrary.monotweety.appdata.status.remote.StatusRemoteRepository
import net.yslibrary.monotweety.di.UserScope
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
