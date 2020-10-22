package net.yslibrary.monotweety.data.session

import androidx.datastore.CorruptionException
import androidx.datastore.DataStore
import androidx.datastore.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

interface SessionRepository {
    val sessionFlow: Flow<Session?>
    suspend fun update(session: Session)
    suspend fun delete()
}

@Singleton
internal class SessionRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<SessionPreferences>,
) : SessionRepository {
    override val sessionFlow: Flow<Session?> = dataStore.data
        .map { it.takeUnless { it.authToken.isEmpty() } }
        .catch { e ->
            if (e is IOException) {
                Timber.e(e, "Error reading SessionPreferences")
                emit(null)
            } else {
                throw e
            }
        }
        .map { it.toEntity() }

    override suspend fun update(session: Session) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setAuthToken(session.authToken)
                .setAuthTokenSecret(session.authTokenSecret)
                .build()
        }
    }

    override suspend fun delete() {
        dataStore.updateData { SessionPreferences.getDefaultInstance() }
    }

    private fun SessionPreferences?.toEntity(): Session? {
        if (this == null) return null
        return Session(
            authToken = this.authToken,
            authTokenSecret = this.authTokenSecret,
        )
    }
}

internal object SessionPreferencesSerializer : Serializer<SessionPreferences> {
    override fun readFrom(input: InputStream): SessionPreferences {
        try {
            return SessionPreferences.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override fun writeTo(t: SessionPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}
