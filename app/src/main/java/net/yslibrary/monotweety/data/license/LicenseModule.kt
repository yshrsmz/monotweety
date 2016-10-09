package net.yslibrary.monotweety.data.license

import dagger.Binds
import dagger.Module
import net.yslibrary.monotweety.base.di.UserScope

/**
 * Created by yshrsmz on 2016/10/10.
 */
@Module
abstract class LicenseModule {

  @UserScope
  @Binds
  abstract fun provideLicenseRepository(repository: LicenseRepositoryImpl): LicenseRepository
}