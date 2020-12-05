package net.yslibrary.monotweety.data

import android.content.Context
import android.content.pm.PackageManager
import com.codingfeline.twitter4kt.core.ConsumerKeys
import com.codingfeline.twitter4kt.core.Twitter
import com.codingfeline.twitter4kt.core.oauth1a.OAuthConfig
import dagger.Module
import dagger.Provides
import kotlinx.datetime.Clock
import net.yslibrary.monotweety.data.auth.AuthDataModule
import net.yslibrary.monotweety.data.session.SessionDataModule
import net.yslibrary.monotweety.data.settings.SettingsDataModule
import net.yslibrary.monotweety.data.twitterapp.TwitterAppDataModule

@Module(
    includes = [
        AuthDataModule::class,
        SessionDataModule::class,
        SettingsDataModule::class,
        TwitterAppDataModule::class,
    ]
)
object SingletonDataModule {

    @Provides
    fun providePackageManager(context: Context): PackageManager = context.packageManager

    @Provides
    fun provideTwitter(
        consumerKeys: ConsumerKeys,
        clock: Clock,
    ): Twitter {
        return Twitter {
            this.consumerKeys = consumerKeys
            this.oAuthConfig = OAuthConfig()
            this.clock = clock
//            this.httpClientConfig = {
//                install(Logging) {
//                    level = LogLevel.ALL
//                    logger = Logger.SIMPLE
//                }
//            }
        }
    }
}
