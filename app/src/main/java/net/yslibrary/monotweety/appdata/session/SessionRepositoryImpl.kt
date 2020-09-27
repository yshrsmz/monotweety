package net.yslibrary.monotweety.appdata.session

import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterSession
import io.reactivex.Single

class SessionRepositoryImpl(private val sessionManager: SessionManager<TwitterSession>) :
    SessionRepository {

    override fun getActiveSession(): Single<Optional<TwitterSession>> {
        return Single.fromCallable { sessionManager.activeSession.toOptional() }
    }
}
