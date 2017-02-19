package net.yslibrary.monotweety.data.config.remote

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.services.ConfigurationService
import net.yslibrary.monotweety.data.config.Configuration
import rx.Single
import com.twitter.sdk.android.core.models.Configuration as TwitterConfig

class ConfigRemoteDataManagerImpl(private val configurationService: ConfigurationService) : ConfigRemoteDataManager {

  override fun get(): Single<Configuration> {
    return Single.fromEmitter<TwitterConfig>({ emitter ->
      val call = configurationService.configuration()

      call.enqueue(object : Callback<TwitterConfig>() {

        override fun success(result: Result<TwitterConfig>) {
          emitter.onSuccess(result.data)
        }

        override fun failure(exception: TwitterException) {
          emitter.onError(exception)
        }
      })
      emitter.setCancellation { call.cancel() }
    })
        .map { Configuration.from(it) }
  }
}