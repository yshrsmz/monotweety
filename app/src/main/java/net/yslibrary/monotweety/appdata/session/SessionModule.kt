package net.yslibrary.monotweety.appdata.session

import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterSession
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SessionModule {

    @Singleton
    @Provides
    fun provideSessionRepository(sessionManager: SessionManager<TwitterSession>): SessionRepository {
        return SessionRepositoryImpl(sessionManager)
    }
}
