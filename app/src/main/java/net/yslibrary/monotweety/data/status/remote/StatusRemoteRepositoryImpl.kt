package net.yslibrary.monotweety.data.status.remote

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.StatusesService
import rx.AsyncEmitter
import rx.Observable
import rx.Single

/**
 * Created by yshrsmz on 2016/09/30.
 */
class StatusRemoteRepositoryImpl(private val statusesService: StatusesService) : StatusRemoteRepository {

  override fun update(status: String, inReplyToStatusId: Long?): Single<Tweet> {
    return Observable.fromEmitter<Tweet>({ emitter ->
      val call = statusesService.update(status, inReplyToStatusId, null, null, null, null, null, null, null)
      call.enqueue(object : Callback<Tweet>() {
        override fun success(result: Result<Tweet>) {
          emitter.onNext(result.data)
          emitter.onCompleted()
        }

        override fun failure(exception: TwitterException) {
          emitter.onError(exception)
        }
      })
      emitter.setCancellation { call.cancel() }
    }, AsyncEmitter.BackpressureMode.BUFFER).toSingle()
  }
}