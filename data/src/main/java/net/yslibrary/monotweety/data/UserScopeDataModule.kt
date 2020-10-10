package net.yslibrary.monotweety.data

import com.codingfeline.twitter4kt.core.Twitter
import com.codingfeline.twitter4kt.core.model.oauth1a.AccessToken
import com.codingfeline.twitter4kt.core.session.ApiClient
import com.codingfeline.twitter4kt.core.startSession
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.data.status.StatusDataModule
import net.yslibrary.monotweety.data.user.UserDataModule
import net.yslibrary.monotweety.di.UserScope

@Module(
    includes = [
        StatusDataModule::class,
        UserDataModule::class,
    ]
)
object UserScopeDataModule {

    @UserScope
    @Provides
    fun provideApiClient(twitter: Twitter, token: AccessToken): ApiClient {
        return twitter.startSession(token)
    }
}
