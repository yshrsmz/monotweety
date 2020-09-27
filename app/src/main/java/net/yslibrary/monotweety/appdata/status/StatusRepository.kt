package net.yslibrary.monotweety.appdata.status

import io.reactivex.Completable

interface StatusRepository {
    fun updateStatus(status: String, inReplyToStatusId: Long? = null): Completable
}
