package net.yslibrary.monotweety.data.config.remote

import io.reactivex.Single
import net.yslibrary.monotweety.data.config.Configuration

interface ConfigRemoteDataManager {
  fun get(): Single<Configuration>
}