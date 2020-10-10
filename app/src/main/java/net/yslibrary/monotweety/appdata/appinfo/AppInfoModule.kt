package net.yslibrary.monotweety.appdata.appinfo

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class AppInfoModule {

    @Singleton
    @Binds
    abstract fun provideAppInfoManager(appInfoManagerImpl: AppInfoManagerImpl): AppInfoManager
}
