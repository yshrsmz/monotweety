package net.yslibrary.monotweety.data.user.local

import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.pushtorefresh.storio3.sqlite.StorIOSQLite
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.local.singleObject
import net.yslibrary.monotweety.data.local.withObject
import net.yslibrary.monotweety.data.user.User
import javax.inject.Inject

@UserScope
class UserLocalRepositoryImpl @Inject constructor(private val storIOSQLite: StorIOSQLite) : UserLocalRepository {

    override fun getById(id: Long): Flowable<Optional<User>> {
        return storIOSQLite.get()
            .singleObject(User::class.java)
            .withQuery(UserTable.queryById(id))
            .prepare()
            .asRxFlowable(BackpressureStrategy.LATEST)
            .map { it.orNull().toOptional() }
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
