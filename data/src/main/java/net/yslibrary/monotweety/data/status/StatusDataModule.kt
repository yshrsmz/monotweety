package net.yslibrary.monotweety.data.status

import com.codingfeline.twitter4kt.core.session.ApiClient
import com.codingfeline.twitter4kt.v1.api.statuses.StatusesApi
import com.codingfeline.twitter4kt.v1.api.statuses.statuses
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.di.UserScope

@Module
internal interface StatusDataModule {
    @Binds
    fun bindStatusRepository(impl: StatusRepositoryImpl): StatusRepository

    companion object {
        @UserScope
        @Provides
        fun provideStatusesApi(apiClient: ApiClient): StatusesApi = apiClient.statuses
    }
}
