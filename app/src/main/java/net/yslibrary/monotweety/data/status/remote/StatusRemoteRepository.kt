package net.yslibrary.monotweety.data.status.remote


import io.reactivex.Single
import net.yslibrary.monotweety.data.status.Tweet

interface StatusRemoteRepository {
  fun update(status: String, inReplyToStatusId: Long? = null): Single<Tweet>
}