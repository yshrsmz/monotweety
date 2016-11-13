package net.yslibrary.monotweety.data.appinfo

import dagger.Binds
import dagger.Module
import net.yslibrary.monotweety.base.di.AppScope

/**
 * Created by yshrsmz on 2016/11/13.
 */
@Module
abstract class AppInfoModule {

  @AppScope
  @Binds
  abstract fun provideAppInfoManager(appInfoManagerImpl: AppInfoManagerImpl): AppInfoManager
}