package net.yslibrary.monotweety.data.session

import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterSession
import rx.Single

class SessionRepositoryImpl(private val sessionManager: SessionManager<TwitterSession>) :
    SessionRepository {

  override fun getActiveSession(): Single<TwitterSession?> {
    return Single.fromCallable { sessionManager.activeSession }
  }
}