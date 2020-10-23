package net.yslibrary.monotweety.data.settings

import androidx.datastore.CorruptionException
import androidx.datastore.DataStore
import androidx.datastore.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import net.yslibrary.monotweety.data.setting.SettingsPreferences
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

interface SettingRepository {
    val settingsFlow: Flow<Settings>
    suspend fun updateNotificationEnabled(enabled: Boolean)
    suspend fun updateFooterEnabled(enabled: Boolean)
    suspend fun updateFooterText(text: String)
    suspend fun updateFooter(enabled: Boolean, text: String)
    suspend fun updateTimelineAppEnabled(enabled: Boolean)
    suspend fun updateTimelineAppPackageName(packageName: String)
    suspend fun updateTimelineApp(enabled: Boolean, packageName: String)
    suspend fun clear()
}

@Singleton
internal class SettingRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<SettingsPreferences>,
) : SettingRepository {
    override val settingsFlow: Flow<Settings> = dataStore.data
        .catch { e ->
            if (e is IOException) {
                Timber.e(e, "Error reading SettingPreferences")
                emit(SettingsPreferences.getDefaultInstance())
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

    override suspend fun updateFooter(enabled: Boolean, text: String) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setFooterEnabled(enabled)
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

    override suspend fun updateTimelineApp(enabled: Boolean, packageName: String) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setTimelineAppEnabled(enabled)
                .setTimelineAppPackageName(packageName)
                .build()
        }
    }

    override suspend fun clear() {
        dataStore.updateData { SettingsPreferences.getDefaultInstance() }
    }

    private fun SettingsPreferences.toEntity(): Settings {
        return Settings(
            notificationEnabled = this.notificationEnabled,
            footerEnabled = this.footerEnabled,
            footerText = this.footerText,
            timelineAppEnabled = this.timelineAppEnabled,
            timelineAppPackageName = this.timelineAppPackageName,
        )
    }
}

internal object SettingPreferencesSerializer : Serializer<SettingsPreferences> {
    override fun readFrom(input: InputStream): SettingsPreferences {
        try {
            return SettingsPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: SettingsPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}
