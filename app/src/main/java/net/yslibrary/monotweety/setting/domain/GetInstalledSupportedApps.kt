package net.yslibrary.monotweety.setting.domain

import io.reactivex.Observable
import io.reactivex.Single
import net.yslibrary.monotweety.appdata.appinfo.AppInfo
import net.yslibrary.monotweety.appdata.appinfo.AppInfoManager
import net.yslibrary.monotweety.appdata.appinfo.TwitterApp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetInstalledSupportedApps @Inject constructor(private val appInfoManager: AppInfoManager) {

    private val twitterApps = TwitterApp.packages()

    fun execute(): Single<List<AppInfo>> {
        return appInfoManager.installedApps()
            .flatMapObservable { Observable.fromIterable(it) }
            .filter { twitterApps.contains(it.packageName) }
            .toList()
    }
}
