package net.yslibrary.monotweety.data.user

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.data.user.local.UserLocalGateway
import net.yslibrary.monotweety.data.user.local.UserLocalGatewayImpl
import net.yslibrary.monotweety.data.user.local.UserPreferencesSerializer
import javax.inject.Singleton

@Module
internal interface UserDataModule {

    @Binds
    fun bindUserLocalGateway(impl: UserLocalGatewayImpl): UserLocalGateway

    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    companion object {
        @Singleton
        @Provides
        fun provideUserDataStore(context: Context): DataStore<UserPreferences> {
            return context.createDataStore(
                fileName = "user_prefs.pb",
                serializer = UserPreferencesSerializer,
            )
        }
    }
}