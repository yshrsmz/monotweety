package net.yslibrary.monotweety.data.session

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal interface SessionDataModule {
    @Binds
    fun bindSessionRepository(impl: SessionRepositoryImpl): SessionRepository

    companion object {
        @Singleton
        @Provides
        fun provideSessionDataStore(context: Context): DataStore<SessionPreferences> {
            return context.createDataStore(
                fileName = "session_prefs.pb",
                serializer = SessionPreferencesSerializer,
            )
        }
    }
}