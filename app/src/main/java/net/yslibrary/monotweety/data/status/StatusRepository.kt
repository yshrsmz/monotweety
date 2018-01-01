package net.yslibrary.monotweety.data.status

import com.gojuno.koptional.Optional
import io.reactivex.Completable
import io.reactivex.Observable

interface StatusRepository {
  fun updateStatus(status: String, inReplyToStatusId: Long? = null): Completable
  fun previousStatus(): Observable<Optional<Tweet>>
  fun clear(): Completable
}