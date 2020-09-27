package net.yslibrary.monotweety.data.setting

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal interface SettingDataModule {
    @Binds
    fun bindSettingRepository(impl: SettingRepositoryImpl): SettingRepository

    companion object {
        @Singleton
        @Provides
        fun provideSettingDataStore(context: Context): DataStore<SettingPreferences> {
            return context.createDataStore(
                fileName = "setting_prefs.pb",
                serializer = SettingPreferencesSerializer,
            )
        }
    }
}