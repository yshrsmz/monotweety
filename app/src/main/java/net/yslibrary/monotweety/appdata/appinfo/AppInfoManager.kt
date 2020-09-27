package net.yslibrary.monotweety.appdata.appinfo

import io.reactivex.Single

interface AppInfoManager {

    fun isInstalled(packageName: String): Single<Boolean>

    fun installedApps(): Single<List<AppInfo>>

    fun appInfo(packageName: String): Single<AppInfo>
}
