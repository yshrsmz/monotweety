package net.yslibrary.monotweety.data.user


import rx.Completable
import rx.Observable

/**
 * Created by yshrsmz on 2016/10/08.
 */
interface UserRepository {
  fun get(id: Long): Observable<User?>
  fun fetch(): Completable
  fun set(user: User): Completable
  fun delete(id: Long): Completable
}