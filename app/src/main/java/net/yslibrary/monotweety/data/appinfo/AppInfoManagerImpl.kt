package net.yslibrary.monotweety.data.appinfo

import android.content.Intent
import android.content.pm.PackageManager
import net.yslibrary.monotweety.base.di.AppScope
import net.yslibrary.monotweety.base.toSingle
import rx.Single
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/11/13.
 */
@AppScope
class AppInfoManagerImpl @Inject constructor(private val packageManager: PackageManager) : AppInfoManager {

  override fun isInstalled(packageName: String): Single<Boolean> {
    return Single.fromCallable {
      try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
      } catch (e: PackageManager.NameNotFoundException) {
        false
      }
    }
  }

  override fun installedApps(): Single<List<AppInfo>> {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)

    return packageManager.queryIntentActivities(intent, 0)
        .map {
          AppInfo(name = it.activityInfo.name,
              packageName = it.activityInfo.packageName,
              installed = true)
        }
        .toSingle()
  }

  override fun appInfo(packageName: String): Single<AppInfo> {
    return Single.fromCallable {
      packageManager.getPackageInfo(packageName, 0)
    }.map { AppInfo(name = it.applicationInfo.name, packageName = it.packageName, installed = true) }
        .onErrorResumeNext { Single.just(AppInfo(name = "", packageName = packageName, installed = false)) }
  }
}