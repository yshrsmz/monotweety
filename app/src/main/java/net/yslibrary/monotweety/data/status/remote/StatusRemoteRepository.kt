package net.yslibrary.monotweety.data.status.remote

import com.twitter.sdk.android.core.models.Tweet
import rx.Completable
import rx.Single

/**
 * Created by yshrsmz on 2016/09/30.
 */
interface StatusRemoteRepository {
  fun update(status: String, inReplyToStatusId: Long? = null): Single<Tweet>
}