package net.yslibrary.monotweety.appdata.license

import dagger.Binds
import dagger.Module
import net.yslibrary.monotweety.base.di.AppScope

@Module
abstract class LicenseModule {

    @AppScope
    @Binds
    abstract fun provideLicenseRepository(repository: LicenseRepositoryImpl): LicenseRepository
}
