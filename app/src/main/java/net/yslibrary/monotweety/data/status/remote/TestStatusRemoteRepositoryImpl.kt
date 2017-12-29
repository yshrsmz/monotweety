package net.yslibrary.monotweety.data.status.remote

import io.reactivex.Single
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.status.Tweet
import javax.inject.Inject

/**
 * This is a test implementation of StatusRemoteRepository.
 * you can switch to this implementation to test editor locally.
 */
@UserScope
class TestStatusRemoteRepositoryImpl @Inject constructor() : StatusRemoteRepository {
  override fun update(status: String, inReplyToStatusId: Long?): Single<Tweet> {
    val tweet = Tweet(
        id = 0,
        inReplyToStatusId = inReplyToStatusId ?: 0,
        text = status,
        createdAt = System.currentTimeMillis().toString()
    )

    return Single.just(tweet)
  }
}