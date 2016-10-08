package net.yslibrary.monotweety.data.user.remote

import com.twitter.sdk.android.core.models.User
import rx.Single

/**
 * Created by yshrsmz on 2016/10/08.
 */
interface UserRemoteRepository {
  fun get(): Single<User>
}