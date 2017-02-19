package net.yslibrary.monotweety.data.status

import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.status.local.StatusLocalRepository
import net.yslibrary.monotweety.data.status.remote.StatusRemoteRepository
import rx.Completable
import rx.Observable
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

  override fun previousStatus(): Observable<Tweet?> {
    return localRepository.getPrevious()
  }

  override fun clear(): Completable {
    return localRepository.clear()
  }
}