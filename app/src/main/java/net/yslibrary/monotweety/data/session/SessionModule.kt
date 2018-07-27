package net.yslibrary.monotweety.data.session

import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterSession
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.AppScope

@Module
class SessionModule {

    @AppScope
    @Provides
    fun provideSessionRepository(sessionManager: SessionManager<TwitterSession>): SessionRepository {
        return SessionRepositoryImpl(sessionManager)
    }
}
