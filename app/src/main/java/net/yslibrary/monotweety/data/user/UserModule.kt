package net.yslibrary.monotweety.data.user

import dagger.Binds
import dagger.Module
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.user.local.UserLocalRepository
import net.yslibrary.monotweety.data.user.local.UserLocalRepositoryImpl
import net.yslibrary.monotweety.data.user.remote.UserRemoteRepository
import net.yslibrary.monotweety.data.user.remote.UserRemoteRepositoryImpl

/**
 * Created by yshrsmz on 2016/09/27.
 */
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