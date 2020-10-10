package net.yslibrary.monotweety.appdata.status

import dagger.Binds
import dagger.Module
import net.yslibrary.monotweety.appdata.status.remote.StatusRemoteRepository
import net.yslibrary.monotweety.appdata.status.remote.StatusRemoteRepositoryImpl
import net.yslibrary.monotweety.di.UserScope

@Module
abstract class StatusModule {

    @UserScope
    @Binds
    abstract fun provideStatusRemoteRepository(repository: StatusRemoteRepositoryImpl): StatusRemoteRepository

    @UserScope
    @Binds
    abstract fun provideStatusRepository(repository: StatusRepositoryImpl): StatusRepository
}
