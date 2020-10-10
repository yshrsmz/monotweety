package net.yslibrary.monotweety.data

import com.codingfeline.twitter4kt.core.ConsumerKeys
import com.codingfeline.twitter4kt.core.Twitter
import com.codingfeline.twitter4kt.core.oauth1a.OAuthConfig
import dagger.Module
import dagger.Provides
import kotlinx.datetime.Clock
import net.yslibrary.monotweety.data.auth.AuthDataModule
import net.yslibrary.monotweety.data.session.SessionDataModule
import net.yslibrary.monotweety.data.setting.SettingDataModule

@Module(
    includes = [
        AuthDataModule::class,
        SessionDataModule::class,
        SettingDataModule::class,
    ]
)
object SingletonDataModule {

    @Provides
    fun provideTwitter(
        consumerKeys: ConsumerKeys,
        clock: Clock
    ): Twitter {
        return Twitter {
            this.consumerKeys = consumerKeys
            this.oAuthConfig = OAuthConfig()
            this.clock = clock
        }
    }
}
