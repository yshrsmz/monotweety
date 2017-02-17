package net.yslibrary.monotweety.data.config

import android.support.annotation.VisibleForTesting
import net.yslibrary.monotweety.base.Clock
import net.yslibrary.monotweety.data.config.local.ConfigLocalDataManager
import net.yslibrary.monotweety.data.config.remote.ConfigRemoteDataManager
import rx.Observable
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions
import timber.log.Timber

/**
 * Created by yshrsmz on 2016/10/01.
 * this class is open just for testing
 */
open class ConfigDataManagerImpl(private val remoteDataManager: ConfigRemoteDataManager,
                                 private val localDataManager: ConfigLocalDataManager,
                                 private val clock: Clock) : ConfigDataManager {

  private var subscription = Subscriptions.unsubscribed()

  override fun shortUrlLengthHttps(): Observable<Int> {
    return localDataManager.shortUrlLengthHttps()
        .doOnNext {
          if (subscription.isUnsubscribed && localDataManager.outdated()) {
            Timber.d("fetch new config from api")
            subscription = remoteDataManager.get()
                .subscribeOn(Schedulers.io())
                .subscribe({
                  updateConfig(it)
                  subscription.unsubscribe()
                }, {
                  Timber.e(it, it.message)
                  subscription.unsubscribe()
                })
          }
        }
  }

  @VisibleForTesting
  open fun updateConfig(configuration: Configuration) {
    Timber.d("update local config: $configuration")
    localDataManager.shortUrlLengthHttps(configuration.shortUrlLengthHttps)
    localDataManager.updatedAt(clock.currentTimeMillis())
  }
}