package net.yslibrary.monotweety.data

import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterSession
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.data.session.SessionModule

/**
 * Created by yshrsmz on 2016/09/27.
 */
@Module(includes = arrayOf(SessionModule::class))
class DataModule {

  @Provides
  fun provideSessionManager(): SessionManager<TwitterSession> {
    return TwitterCore.getInstance().sessionManager
  }
}