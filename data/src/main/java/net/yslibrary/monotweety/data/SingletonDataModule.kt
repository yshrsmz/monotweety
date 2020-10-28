package net.yslibrary.monotweety.data

import com.codingfeline.twitter4kt.core.ConsumerKeys
import com.codingfeline.twitter4kt.core.Twitter
import com.codingfeline.twitter4kt.core.oauth1a.OAuthConfig
import dagger.Module
import dagger.Provides
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import kotlinx.datetime.Clock
import net.yslibrary.monotweety.data.auth.AuthDataModule
import net.yslibrary.monotweety.data.session.SessionDataModule
import net.yslibrary.monotweety.data.settings.SettingsDataModule

@Module(
    includes = [
        AuthDataModule::class,
        SessionDataModule::class,
        SettingsDataModule::class,
    ]
)
object SingletonDataModule {

    @Provides
    fun provideTwitter(
        consumerKeys: ConsumerKeys,
        clock: Clock,
    ): Twitter {
        return Twitter {
            this.consumerKeys = consumerKeys
            this.oAuthConfig = OAuthConfig()
            this.clock = clock
            this.httpClientConfig = {
                install(Logging) {
                    level = LogLevel.ALL
                    logger = Logger.SIMPLE
                }
            }
        }
    }
}
