package net.yslibrary.monotweety.data.status

import com.gojuno.koptional.Optional
import io.reactivex.Completable
import io.reactivex.Observable
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.status.local.StatusLocalRepository
import net.yslibrary.monotweety.data.status.remote.StatusRemoteRepository
import timber.log.Timber
import javax.inject.Inject

@UserScope
class StatusRepositoryImpl @Inject constructor(private val remoteRepository: StatusRemoteRepository,
                                               private val localRepository: StatusLocalRepository) : StatusRepository {

    override fun updateStatus(status: String, inReplyToStatusId: Long?): Completable {
        return remoteRepository.update(status, inReplyToStatusId)
            .doOnSuccess {
                Timber.d("status updated: ${it.text}")
            }
            .flatMapCompletable { localRepository.update(it) }
    }

    override fun previousStatus(): Observable<Optional<Tweet>> {
        return localRepository.getPrevious()
    }

    override fun clear(): Completable {
        return localRepository.clear()
    }
}
