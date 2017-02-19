package net.yslibrary.monotweety.data.session

import com.twitter.sdk.android.core.TwitterSession
import rx.Single

interface SessionRepository {
  fun getActiveSession(): Single<TwitterSession?>
}