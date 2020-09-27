package net.yslibrary.monotweety.appdata.appinfo

import dagger.Binds
import dagger.Module
import net.yslibrary.monotweety.base.di.AppScope

@Module
abstract class AppInfoModule {

    @AppScope
    @Binds
    abstract fun provideAppInfoManager(appInfoManagerImpl: AppInfoManagerImpl): AppInfoManager
}
