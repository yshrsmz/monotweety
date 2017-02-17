package net.yslibrary.monotweety.setting.domain

import net.yslibrary.monotweety.base.di.AppScope
import net.yslibrary.monotweety.data.appinfo.AppInfo
import net.yslibrary.monotweety.data.appinfo.AppInfoManager
import net.yslibrary.monotweety.data.appinfo.TwitterApp
import rx.Observable
import rx.Single
import javax.inject.Inject

@AppScope
class GetInstalledSupportedApps @Inject constructor(private val appInfoManager: AppInfoManager) {

  private val twitterApps = TwitterApp.packages()

  fun execute(): Single<List<AppInfo>> {
    return appInfoManager.installedApps()
        .flatMapObservable { Observable.from(it) }
        .filter { twitterApps.contains(it.packageName) }
        .toList().toSingle()
  }
}