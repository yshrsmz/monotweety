package net.yslibrary.monotweety.data.config.remote

import net.yslibrary.monotweety.data.config.Configuration
import rx.Single

interface ConfigRemoteDataManager {
  fun get(): Single<Configuration>
}