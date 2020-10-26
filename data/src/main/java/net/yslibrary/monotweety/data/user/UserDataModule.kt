package net.yslibrary.monotweety.data.user

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import com.codingfeline.twitter4kt.core.session.ApiClient
import com.codingfeline.twitter4kt.v1.api.account.AccountApi
import com.codingfeline.twitter4kt.v1.api.account.account
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.data.user.local.UserLocalGateway
import net.yslibrary.monotweety.data.user.local.UserLocalGatewayImpl
import net.yslibrary.monotweety.data.user.local.UserPreferencesSerializer
import net.yslibrary.monotweety.data.user.remote.UserRemoteGateway
import net.yslibrary.monotweety.data.user.remote.UserRemoteGatewayImpl
import net.yslibrary.monotweety.di.UserScope

@Module
internal interface UserDataModule {

    @Binds
    fun bindUserLocalGateway(impl: UserLocalGatewayImpl): UserLocalGateway

    @Binds
    fun bindUserRemoteGateway(impl: UserRemoteGatewayImpl): UserRemoteGateway

    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    companion object {

        @UserScope
        @Provides
        fun provideUserDataStore(context: Context): DataStore<UserPreferences> {
            return context.createDataStore(
                fileName = "user_prefs.pb",
                serializer = UserPreferencesSerializer,
            )
        }

        @UserScope
        @Provides
        fun provideAccountApi(apiClient: ApiClient): AccountApi = apiClient.account
    }
}
