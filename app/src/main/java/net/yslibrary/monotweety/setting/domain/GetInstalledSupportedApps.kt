package net.yslibrary.monotweety.setting.domain

import io.reactivex.Observable
import io.reactivex.Single
import net.yslibrary.monotweety.base.di.AppScope
import net.yslibrary.monotweety.data.appinfo.AppInfo
import net.yslibrary.monotweety.data.appinfo.AppInfoManager
import net.yslibrary.monotweety.data.appinfo.TwitterApp
import javax.inject.Inject

@AppScope
class GetInstalledSupportedApps @Inject constructor(private val appInfoManager: AppInfoManager) {

    private val twitterApps = TwitterApp.packages()

    fun execute(): Single<List<AppInfo>> {
        return appInfoManager.installedApps()
            .flatMapObservable { Observable.fromIterable(it) }
            .filter { twitterApps.contains(it.packageName) }
            .toList()
    }
}
