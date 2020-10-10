package net.yslibrary.monotweety.appdata.user

import dagger.Binds
import dagger.Module
import net.yslibrary.monotweety.appdata.user.local.UserLocalRepository
import net.yslibrary.monotweety.appdata.user.local.UserLocalRepositoryImpl
import net.yslibrary.monotweety.appdata.user.remote.UserRemoteRepository
import net.yslibrary.monotweety.appdata.user.remote.UserRemoteRepositoryImpl
import net.yslibrary.monotweety.di.UserScope

@Module
abstract class UserModule {

    @UserScope
    @Binds
    abstract fun bindUserRemoteRepository(repository: UserRemoteRepositoryImpl): UserRemoteRepository

    @UserScope
    @Binds
    abstract fun bindUserLocalRepository(repository: UserLocalRepositoryImpl): UserLocalRepository

    @UserScope
    @Binds
    abstract fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

    interface Provider {
        fun userRepository(): UserRepository
    }
}
