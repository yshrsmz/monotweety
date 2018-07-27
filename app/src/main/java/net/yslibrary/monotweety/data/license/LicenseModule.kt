package net.yslibrary.monotweety.data.license

import dagger.Binds
import dagger.Module
import net.yslibrary.monotweety.base.di.UserScope

@Module
abstract class LicenseModule {

    @UserScope
    @Binds
    abstract fun provideLicenseRepository(repository: LicenseRepositoryImpl): LicenseRepository
}
