package net.yslibrary.monotweety.data.auth

import com.codingfeline.twitter4kt.core.Twitter
import com.codingfeline.twitter4kt.core.launchOAuth1aFlow
import com.codingfeline.twitter4kt.core.oauth1a.OAuth1aFlow
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
internal interface AuthDataModule {

    @Binds
    fun bindAuthFlow(impl: AuthFlowImpl): AuthFlow

    companion object {
        @Provides
        fun provideOAuthFlow(twitter: Twitter): OAuth1aFlow {
            return twitter.launchOAuth1aFlow()
        }
    }
}
