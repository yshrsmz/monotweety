package net.yslibrary.monotweety.data.user


import rx.Completable
import rx.Observable

interface UserRepository {
  fun get(id: Long): Observable<User?>
  fun fetch(): Completable
  fun set(user: User): Completable
  fun delete(id: Long): Completable
  fun isValid(user: User?): Boolean
}