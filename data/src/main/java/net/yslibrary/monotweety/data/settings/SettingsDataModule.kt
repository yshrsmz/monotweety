package net.yslibrary.monotweety.data.settings

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.data.setting.SettingsPreferences
import javax.inject.Singleton

@Module
internal interface SettingsDataModule {
    @Binds
    fun bindSettingsRepository(impl: SettingRepositoryImpl): SettingRepository

    companion object {
        @Singleton
        @Provides
        fun provideSettingsDataStore(context: Context): DataStore<SettingsPreferences> {
            return context.createDataStore(
                fileName = "setting_prefs.pb",
                serializer = SettingPreferencesSerializer,
            )
        }
    }
}
