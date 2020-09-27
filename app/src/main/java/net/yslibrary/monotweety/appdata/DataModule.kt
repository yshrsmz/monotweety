package net.yslibrary.monotweety.appdata

import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterSession
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.appdata.appinfo.AppInfoModule
import net.yslibrary.monotweety.appdata.license.LicenseModule
import net.yslibrary.monotweety.appdata.local.LocalModule
import net.yslibrary.monotweety.appdata.session.SessionModule
import net.yslibrary.monotweety.appdata.setting.SettingModule

@Module(
    includes = [
        AppInfoModule::class,
        LicenseModule::class,
        LocalModule::class,
        SessionModule::class,
        SettingModule::class,
    ]
)
class DataModule {

    @Provides
    fun provideSessionManager(): SessionManager<TwitterSession> {
        return TwitterCore.getInstance().sessionManager
    }
}
