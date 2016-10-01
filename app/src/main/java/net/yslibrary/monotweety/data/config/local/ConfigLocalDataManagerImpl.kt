package net.yslibrary.monotweety.data.config.local

import com.f2prateek.rx.preferences.RxSharedPreferences
import net.yslibrary.monotweety.base.Clock
import rx.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by yshrsmz on 2016/10/01.
 */
class ConfigLocalDataManagerImpl(private val prefs: RxSharedPreferences,
                                 private val clock: Clock) : ConfigLocalDataManager {

  private val shortUrlLengthHttps = prefs.getInteger(SHORT_URL_LENGTH_HTTPS, 23)
  private val updatedAt = prefs.getLong(UPDATED_AT, 0)

  override fun shortUrlLengthHttps(): Observable<Int> {
    return shortUrlLengthHttps.asObservable()
  }

  override fun shortUrlLengthHttps(length: Int) {
    return shortUrlLengthHttps.set(length)
  }

  override fun updatedAt(): Long {
    return updatedAt.get()!!
  }

  override fun updatedAt(timestamp: Long) {
    updatedAt.set(timestamp)
  }

  override fun outdated(): Boolean {
    return (updatedAt() + TimeUnit.HOURS.toMillis(12)) <= clock.currentTimeMillis()
  }

  companion object {
    const val SHORT_URL_LENGTH_HTTPS = "short_url_length_https"
    const val UPDATED_AT = "updated_at"
  }
}