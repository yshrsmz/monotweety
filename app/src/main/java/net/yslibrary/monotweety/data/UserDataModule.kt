package net.yslibrary.monotweety.data

import com.twitter.Extractor
import com.twitter.Validator
import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.services.ConfigurationService
import com.twitter.sdk.android.core.services.StatusesService
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.config.ConfigModule
import net.yslibrary.monotweety.data.status.StatusModule

/**
 * Created by yshrsmz on 2016/09/27.
 */
@Module(
    includes = arrayOf(StatusModule::class, ConfigModule::class)
)
class UserDataModule {

  @Provides
  fun provideTwitterStatusService(sessionManager: SessionManager<TwitterSession>): StatusesService {
    return TwitterCore.getInstance().getApiClient(sessionManager.activeSession).statusesService
  }

  @Provides
  fun provideTwitterConfigurationService(sessionManager: SessionManager<TwitterSession>): ConfigurationService {
    return TwitterCore.getInstance().getApiClient(sessionManager.activeSession).configurationService
  }

  @UserScope
  @Provides
  fun provideValidator(): Validator {
    return Validator()
  }

  @UserScope
  @Provides
  fun provideExtractor(): Extractor {
    val extractor = Extractor()
    extractor.isExtractURLWithoutProtocol = true
    return extractor
  }
}