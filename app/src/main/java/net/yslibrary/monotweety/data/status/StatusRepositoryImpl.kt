package net.yslibrary.monotweety.data.status

import net.yslibrary.monotweety.data.status.remote.StatusRemoteRepository
import rx.Completable
import timber.log.Timber

/**
 * Created by yshrsmz on 2016/09/30.
 */
class StatusRepositoryImpl(private val remoteRepository: StatusRemoteRepository) : StatusRepository {
  override fun updateStatus(status: String, inReplyToStatusId: Long?): Completable {
    return remoteRepository.update(status, inReplyToStatusId)
        .doOnSuccess { Timber.d("status updated: $it") } // TODO: save status here
        .toCompletable()
  }
}