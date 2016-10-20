package net.yslibrary.monotweety.data.status

import dagger.Binds
import dagger.Module
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.status.local.StatusLocalRepository
import net.yslibrary.monotweety.data.status.local.StatusLocalRepositoryImpl
import net.yslibrary.monotweety.data.status.remote.StatusRemoteRepository
import net.yslibrary.monotweety.data.status.remote.StatusRemoteRepositoryImpl

/**
 * Created by yshrsmz on 2016/09/30.
 */
@Module
abstract class StatusModule {

  @UserScope
  @Binds
  abstract fun provideStatusRemoteRepository(repository: StatusRemoteRepositoryImpl): StatusRemoteRepository

  @UserScope
  @Binds
  abstract fun provideStatusLocalRepository(repository: StatusLocalRepositoryImpl): StatusLocalRepository

  @UserScope
  @Binds
  abstract fun provideStatusRepository(repository: StatusRepositoryImpl): StatusRepository
}