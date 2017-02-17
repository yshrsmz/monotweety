package net.yslibrary.monotweety.data.status

import rx.Completable
import rx.Observable

interface StatusRepository {
  fun updateStatus(status: String, inReplyToStatusId: Long? = null): Completable
  fun previousStatus(): Observable<Tweet?>
  fun clear(): Completable
}