package net.yslibrary.monotweety.data.setting

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

interface SettingRepository {
    val settingFlow: Flow<Setting>
    suspend fun updateNotificationEnabled(enabled: Boolean)
    suspend fun updateFooterEnabled(enabled: Boolean)
    suspend fun updateFooterText(text: String)
    suspend fun updateTimelineAppEnabled(enabled: Boolean)
    suspend fun updateTimelineAppPackageName(packageName: String)
    suspend fun clear()
}

@Singleton
internal class SettingRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<SettingPreferences>,
) : SettingRepository {
    override val settingFlow: Flow<Setting> = dataStore.data
        .catch { e ->
            if (e is IOException) {
                Timber.e(e, "Error reading SettingPreferences")
                emit(SettingPreferences.getDefaultInstance())
            } else {
                throw e
            }
        }
        .map { it.toEntity() }

    override suspend fun updateNotificationEnabled(enabled: Boolean) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setNotificationEnabled(enabled)
                .build()
        }
    }

    override suspend fun updateFooterEnabled(enabled: Boolean) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setFooterEnabled(enabled)
                .build()
        }
    }

    override suspend fun updateFooterText(text: String) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setFooterText(text)
                .build()
        }
    }

    override suspend fun updateTimelineAppEnabled(enabled: Boolean) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setTimelineAppEnabled(enabled)
                .build()
        }
    }

    override suspend fun updateTimelineAppPackageName(packageName: String) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setTimelineAppPackageName(packageName)
                .build()
        }
    }

    override suspend fun clear() {
        dataStore.updateData { SettingPreferences.getDefaultInstance() }
    }

    private fun SettingPreferences.toEntity(): Setting {
        return Setting(
            notificationEnabled = this.notificationEnabled,
            footerEnabled = this.footerEnabled,
            footerText = this.footerText,
            timelineAppEnabled = this.timelineAppEnabled,
            timelineAppPackageName = this.timelineAppPackageName,
        )
    }
}

internal object SettingPreferencesSerializer : Serializer<SettingPreferences> {
    override fun readFrom(input: InputStream): SettingPreferences {
        try {
            return SettingPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: SettingPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}