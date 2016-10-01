package net.yslibrary.monotweety.data.config.remote

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Configuration
import com.twitter.sdk.android.core.services.ConfigurationService
import rx.AsyncEmitter
import rx.Observable
import rx.Single

/**
 * Created by yshrsmz on 2016/10/01.
 */
class ConfigRemoteDataManagerImpl(private val configurationService: ConfigurationService) : ConfigRemoteDataManager {

  override fun get(): Single<Configuration> {
    return Observable.fromEmitter<Configuration>({ emitter ->
      val call = configurationService.configuration()

      call.enqueue(object : Callback<Configuration>() {

        override fun success(result: Result<Configuration>) {
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