package net.yslibrary.monotweety.data.user.local

import androidx.datastore.CorruptionException
import androidx.datastore.DataStore
import androidx.datastore.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import net.yslibrary.monotweety.data.user.UserPreferences
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

interface UserLocalGateway {
    val user: Flow<UserPreferences?>

    suspend fun update(user: UserPreferences)
}

internal class UserLocalGatewayImpl(
    private val dataStore: DataStore<UserPreferences>
) : UserLocalGateway {
    override val user: Flow<UserPreferences?> = dataStore.data
        .map { it.takeUnless { it.updatedAt == 0L } }
        .catch { e ->
            if (e is IOException) {
                Timber.e(e, "Error reading UsePreferences")
                emit(null)
            } else {
                throw e
            }
        }

    override suspend fun update(user: UserPreferences) {
        dataStore.updateData { user }
    }
}

internal object UserPreferencesSerializer : Serializer<UserPreferences> {
    override fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: UserPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}