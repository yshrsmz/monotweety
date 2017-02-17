package net.yslibrary.monotweety.data.status

import rx.Completable
import rx.Observable

/**
 * Created by yshrsmz on 2016/09/30.
 */
interface StatusRepository {
  fun updateStatus(status: String, inReplyToStatusId: Long? = null): Completable
  fun previousStatus(): Observable<Tweet?>
  fun clear(): Completable
}