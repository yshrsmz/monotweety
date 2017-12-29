package net.yslibrary.monotweety.data.config

import android.support.annotation.VisibleForTesting
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import net.yslibrary.monotweety.base.Clock
import net.yslibrary.monotweety.data.config.local.ConfigLocalDataManager
import net.yslibrary.monotweety.data.config.remote.ConfigRemoteDataManager
import timber.log.Timber

/**
 * Created by yshrsmz on 2016/10/01.
 * this class is open just for testing
 */
open class ConfigDataManagerImpl(private val remoteDataManager: ConfigRemoteDataManager,
                                 private val localDataManager: ConfigLocalDataManager,
                                 private val clock: Clock) : ConfigDataManager {

  private var disposable = Disposables.disposed()

  override fun shortUrlLengthHttps(): Observable<Int> {
    return localDataManager.shortUrlLengthHttps()
        .doOnNext {
          if (disposable.isDisposed && localDataManager.outdated()) {
            Timber.d("fetch new config from api")
            disposable = remoteDataManager.get()
                .subscribeOn(Schedulers.io())
                .subscribe({
                  updateConfig(it)
                  disposable.dispose()
                }, {
                  Timber.e(it, it.message)
                  disposable.dispose()
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