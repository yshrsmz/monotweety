package net.yslibrary.monotweety.data.status

import com.twitter.sdk.android.core.services.StatusesService
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.status.remote.StatusRemoteRepository
import net.yslibrary.monotweety.data.status.remote.StatusRemoteRepositoryImpl

/**
 * Created by yshrsmz on 2016/09/30.
 */
@Module
class StatusModule {

  @UserScope
  @Provides
  fun provideStatusRemoteRepository(statusesService: StatusesService): StatusRemoteRepository {
    return StatusRemoteRepositoryImpl(statusesService)
  }

  @UserScope
  @Provides
  fun provideStatusRepository(statusRemoteRepository: StatusRemoteRepository): StatusRepository {
    return StatusRepositoryImpl(statusRemoteRepository)
  }
}