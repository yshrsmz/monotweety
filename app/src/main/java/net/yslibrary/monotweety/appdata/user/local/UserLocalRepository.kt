package net.yslibrary.monotweety.appdata.user.local

import com.gojuno.koptional.Optional
import io.reactivex.Completable
import io.reactivex.Flowable
import net.yslibrary.monotweety.appdata.user.User

interface UserLocalRepository {
    fun getById(id: Long): Flowable<Optional<User>>
    fun set(entity: User): Completable
    fun delete(id: Long): Completable
}
