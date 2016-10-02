package net.yslibrary.monotweety.data.status

import com.twitter.sdk.android.core.models.Tweet
import rx.Completable
import rx.Observable

/**
 * Created by yshrsmz on 2016/09/30.
 */
interface StatusRepository {
  fun updateStatus(status: String, inReplyToStatusId: Long? = null): Completable
  fun previousStatus(): Observable<Tweet?>
}