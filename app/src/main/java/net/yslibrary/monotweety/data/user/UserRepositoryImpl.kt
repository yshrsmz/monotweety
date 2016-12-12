package net.yslibrary.monotweety.data.user

import net.yslibrary.monotweety.base.Clock
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.user.local.UserLocalRepository
import net.yslibrary.monotweety.data.user.remote.UserRemoteRepository
import rx.Completable
import rx.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/10/08.
 */
@UserScope
class UserRepositoryImpl @Inject constructor(private val remoteRepository: UserRemoteRepository,
                                             private val localRepository: UserLocalRepository,
                                             private val clock: Clock) : UserRepository {

  override fun get(id: Long): Observable<User?> {
    return localRepository.getById(id)
  }

  override fun delete(id: Long): Completable {
    return localRepository.delete(id)
  }

  override fun fetch(): Completable {
    return remoteRepository.get()
        .map { it.copy(_updatedAt = clock.currentTimeMillis()) }
        .flatMapCompletable { localRepository.set(it) }
  }

  override fun set(user: User): Completable {
    return localRepository.set(user)
  }

  override fun isValid(user: User?): Boolean {
    if (user == null) {
      return false
    }

    val diff = clock.currentTimeMillis() - user._updatedAt
    return TimeUnit.MILLISECONDS.toHours(diff) < 12
  }
}