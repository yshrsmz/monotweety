package net.yslibrary.monotweety.data.user.local

import com.pushtorefresh.storio.sqlite.StorIOSQLite
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.local.singleObject
import net.yslibrary.monotweety.data.local.withObject
import net.yslibrary.monotweety.data.user.User
import rx.Completable
import rx.Observable
import javax.inject.Inject

@UserScope
class UserLocalRepositoryImpl @Inject constructor(private val storIOSQLite: StorIOSQLite) : UserLocalRepository {

  override fun getById(id: Long): Observable<User?> {
    return storIOSQLite.get()
        .singleObject(User::class.java)
        .withQuery(UserTable.queryById(id))
        .prepare()
        .asRxObservable()
  }

  override fun set(entity: User): Completable {
    return storIOSQLite.put()
        .withObject(entity)
        .prepare()
        .asRxCompletable()
  }

  override fun delete(id: Long): Completable {
    return storIOSQLite.delete()
        .byQuery(UserTable.deleteById(id))
        .prepare()
        .asRxCompletable()
  }
}