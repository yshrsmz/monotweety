package net.yslibrary.monotweety.data

import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.services.StatusesService
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.data.status.StatusModule

/**
 * Created by yshrsmz on 2016/09/27.
 */
@Module(
    includes = arrayOf(StatusModule::class)
)
class UserDataModule {

  @Provides
  fun provideTwitterStatusService(sessionManager: SessionManager<TwitterSession>): StatusesService {
    return TwitterCore.getInstance().getApiClient(sessionManager.activeSession).statusesService
  }
}