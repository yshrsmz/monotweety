package net.yslibrary.monotweety.data.status.remote


import net.yslibrary.monotweety.data.status.Tweet
import rx.Single

/**
 * Created by yshrsmz on 2016/09/30.
 */
interface StatusRemoteRepository {
  fun update(status: String, inReplyToStatusId: Long? = null): Single<Tweet>
}