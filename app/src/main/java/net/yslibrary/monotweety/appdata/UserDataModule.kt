package net.yslibrary.monotweety.appdata

import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.services.AccountService
import com.twitter.sdk.android.core.services.ConfigurationService
import com.twitter.twittertext.Extractor
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.appdata.status.StatusModule
import net.yslibrary.monotweety.appdata.status.remote.TwitterApiClient
import net.yslibrary.monotweety.appdata.status.remote.UpdateStatusService
import net.yslibrary.monotweety.appdata.user.UserModule
import net.yslibrary.monotweety.di.UserScope
import java.util.concurrent.ConcurrentHashMap

@Module(
    includes = [StatusModule::class, UserModule::class]
)
class UserDataModule {

    val apiClients = ConcurrentHashMap<TwitterSession, TwitterApiClient>()

    private fun getApiClient(session: TwitterSession): TwitterApiClient {
        if (!apiClients.containsKey(session)) {
            apiClients.putIfAbsent(session, TwitterApiClient(session))
        }
        return apiClients[session]!!
    }

    @Provides
    fun provideTwitterStatusService(sessionManager: SessionManager<TwitterSession>): UpdateStatusService {
        return getApiClient(sessionManager.activeSession).updateStatusService
    }

    @Provides
    fun provideTwitterConfigurationService(sessionManager: SessionManager<TwitterSession>): ConfigurationService {
        return getApiClient(sessionManager.activeSession).configurationService
    }

    @Provides
    fun provideTwitterAccountService(sessionManager: SessionManager<TwitterSession>): AccountService {
        return getApiClient(sessionManager.activeSession).accountService
    }

    @UserScope
    @Provides
    fun provideExtractor(): Extractor {
        val extractor = Extractor()
        extractor.isExtractURLWithoutProtocol = true
        return extractor
    }
}
