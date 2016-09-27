package net.yslibrary.monotweety.data.session

import com.twitter.sdk.android.core.TwitterSession
import rx.Single

/**
 * Created by yshrsmz on 2016/09/27.
 */
interface SessionRepository {
  fun getActiveSession(): Single<TwitterSession?>
}