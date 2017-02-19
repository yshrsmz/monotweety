package net.yslibrary.monotweety.data.user.remote

import net.yslibrary.monotweety.data.user.User
import rx.Single

interface UserRemoteRepository {
  fun get(): Single<User>
}