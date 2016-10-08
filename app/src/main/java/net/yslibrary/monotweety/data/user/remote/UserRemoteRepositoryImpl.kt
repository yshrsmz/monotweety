package net.yslibrary.monotweety.data.user.remote

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.User
import com.twitter.sdk.android.core.services.AccountService
import net.yslibrary.monotweety.base.di.UserScope
import rx.AsyncEmitter
import rx.Observable
import rx.Single
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/10/08.
 */
@UserScope
class UserRemoteRepositoryImpl @Inject constructor(private val accountService: AccountService) : UserRemoteRepository {
  override fun get(): Single<User> {
    return Observable.fromEmitter<User>({ emitter ->
      val call = accountService.verifyCredentials(false, true)
      call.enqueue(object : Callback<User>() {
        override fun failure(exception: TwitterException?) {
          emitter.onError(exception)
        }

        override fun success(result: Result<User>) {
          emitter.onNext(result.data)
          emitter.onCompleted()
        }
      })
      emitter.setCancellation { call.cancel() }
    }, AsyncEmitter.BackpressureMode.BUFFER).toSingle()
  }
}