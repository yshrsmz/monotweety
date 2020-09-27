package net.yslibrary.monotweety.appdata.status.remote


import io.reactivex.Single
import net.yslibrary.monotweety.appdata.status.Tweet

interface StatusRemoteRepository {
    fun update(status: String, inReplyToStatusId: Long? = null): Single<Tweet>
}
