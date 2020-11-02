package net.yslibrary.monotweety.data.appinfo

import dagger.Binds
import dagger.Module

@Module
internal interface AppInfoDataModule {
    @Binds
    fun bindTwitterAppRepository(impl: TwitterAppRepositoryImpl): TwitterAppRepository
}
