package net.yslibrary.monotweety.appdata.license

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class LicenseModule {

    @Singleton
    @Binds
    abstract fun provideLicenseRepository(repository: LicenseRepositoryImpl): LicenseRepository
}
