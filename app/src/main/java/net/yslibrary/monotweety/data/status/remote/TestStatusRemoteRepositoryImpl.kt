package net.yslibrary.monotweety.data.status.remote

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.TweetBuilder
import net.yslibrary.monotweety.base.di.UserScope
import rx.Single
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/10/18.
 */
@UserScope
class TestStatusRemoteRepositoryImpl @Inject constructor() : StatusRemoteRepository {
  override fun update(status: String, inReplyToStatusId: Long?): Single<Tweet> {
    val builder = TweetBuilder()
        .setText(status)
        .setId(System.currentTimeMillis())
        .setCreatedAt(System.currentTimeMillis().toString())

    inReplyToStatusId?.let { builder.setInReplyToStatusId(it) }

    return Single.just(builder.build())
  }
}