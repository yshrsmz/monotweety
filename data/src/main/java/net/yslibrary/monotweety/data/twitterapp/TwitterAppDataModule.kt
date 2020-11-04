package net.yslibrary.monotweety.data.twitterapp

import dagger.Binds
import dagger.Module

@Module
internal interface TwitterAppDataModule {
    @Binds
    fun bindTwitterAppRepository(impl: TwitterAppRepositoryImpl): TwitterAppRepository
}
