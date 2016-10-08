package net.yslibrary.monotweety.data.user.local

import net.yslibrary.monotweety.data.user.User
import rx.Completable
import rx.Observable

/**
 * Created by yshrsmz on 2016/09/27.
 */
interface UserLocalRepository {
  fun getById(id: Long): Observable<User?>
  fun set(entity: User): Completable
  fun delete(id: Long): Completable
}