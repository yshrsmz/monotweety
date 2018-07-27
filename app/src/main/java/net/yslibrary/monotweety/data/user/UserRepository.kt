package net.yslibrary.monotweety.data.user


import com.gojuno.koptional.Optional
import io.reactivex.Completable
import io.reactivex.Flowable

interface UserRepository {
    fun get(id: Long): Flowable<Optional<User>>
    fun fetch(): Completable
    fun set(user: User): Completable
    fun delete(id: Long): Completable
    fun isValid(user: User?): Boolean
}
