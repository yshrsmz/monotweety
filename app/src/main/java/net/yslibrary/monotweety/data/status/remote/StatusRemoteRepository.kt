package net.yslibrary.monotweety.data.status.remote


import net.yslibrary.monotweety.data.status.Tweet
import rx.Single

interface StatusRemoteRepository {
  fun update(status: String, inReplyToStatusId: Long? = null): Single<Tweet>
}