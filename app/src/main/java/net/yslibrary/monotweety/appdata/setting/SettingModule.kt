package net.yslibrary.monotweety.appdata.setting

import android.content.Context
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.Names
import javax.inject.Named
import javax.inject.Singleton

@Module
class SettingModule {

    @Named(Names.FOR_SETTING)
    @Singleton
    @Provides
    fun provideSettingPreferences(context: Context): RxSharedPreferences {
        val prefs = context.getSharedPreferences(
            "net.yslibrary.monotweety.prefs.settings",
            Context.MODE_PRIVATE
        )
        return RxSharedPreferences.create(prefs)
    }

    @Singleton
    @Provides
    fun provideSettingDataManager(@Named(Names.FOR_SETTING) prefs: RxSharedPreferences): SettingDataManager {
        return SettingDataManagerImpl(prefs)
    }

    interface Provider {
        fun settingDataManager(): SettingDataManager
    }
}
