package net.yslibrary.monotweety.data.config.remote

import com.twitter.sdk.android.core.models.Configuration
import rx.Single

/**
 * Created by yshrsmz on 2016/10/01.
 */
interface ConfigRemoteDataManager {
  fun get(): Single<Configuration>
}