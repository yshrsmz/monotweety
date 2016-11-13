package net.yslibrary.monotweety.data.appinfo

import rx.Single

/**
 * Created by yshrsmz on 2016/11/13.
 */
interface AppInfoManager {

  fun isInstalled(packageName: String): Boolean

  fun installedApps(): Single<List<AppInfo>>

  fun appInfo(packageName: String): Single<AppInfo>
}