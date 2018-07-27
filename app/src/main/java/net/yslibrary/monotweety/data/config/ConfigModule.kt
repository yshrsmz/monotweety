package net.yslibrary.monotweety.data.config

import android.content.Context
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.twitter.sdk.android.core.services.ConfigurationService
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.Clock
import net.yslibrary.monotweety.base.di.Names
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.config.local.ConfigLocalDataManager
import net.yslibrary.monotweety.data.config.local.ConfigLocalDataManagerImpl
import net.yslibrary.monotweety.data.config.remote.ConfigRemoteDataManager
import net.yslibrary.monotweety.data.config.remote.ConfigRemoteDataManagerImpl
import javax.inject.Named

@Module
class ConfigModule {

    @Named(Names.FOR_CONFIG)
    @UserScope
    @Provides
    fun provideConfigPreferences(@Named(Names.FOR_APP) context: Context): RxSharedPreferences {
        val prefs = context.getSharedPreferences("net.yslibrary.monotweety.prefs.config", Context.MODE_PRIVATE)
        return RxSharedPreferences.create(prefs)
    }

    @UserScope
    @Provides
    fun provideConfigLocalDataManager(@Named(Names.FOR_CONFIG) prefs: RxSharedPreferences,
                                      clock: Clock): ConfigLocalDataManager {
        return ConfigLocalDataManagerImpl(prefs, clock)
    }

    @UserScope
    @Provides
    fun provideConfigRemoteDataManager(configurationService: ConfigurationService): ConfigRemoteDataManager {
        return ConfigRemoteDataManagerImpl(configurationService)
    }

    @UserScope
    @Provides
    fun provideConfigDataManager(configRemoteDataManager: ConfigRemoteDataManager,
                                 configLocalDataManager: ConfigLocalDataManager,
                                 clock: Clock): ConfigDataManager {
        return ConfigDataManagerImpl(configRemoteDataManager, configLocalDataManager, clock)
    }
}
