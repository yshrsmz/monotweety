package net.yslibrary.monotweety.appdata.user.remote

import io.reactivex.Single
import net.yslibrary.monotweety.appdata.user.User

interface UserRemoteRepository {
    fun get(): Single<User>
}
