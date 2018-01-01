package net.yslibrary.monotweety.data.user.remote

import io.reactivex.Single
import net.yslibrary.monotweety.data.user.User

interface UserRemoteRepository {
  fun get(): Single<User>
}