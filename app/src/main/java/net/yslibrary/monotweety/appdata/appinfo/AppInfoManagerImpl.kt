package net.yslibrary.monotweety.appdata.appinfo

import android.content.Intent
import android.content.pm.PackageManager
import io.reactivex.Single
import net.yslibrary.monotweety.base.toSingle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInfoManagerImpl @Inject constructor(private val packageManager: PackageManager) :
    AppInfoManager {

    override fun isInstalled(packageName: String): Single<Boolean> {
        return Single.fromCallable {
            try {
                packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }
    }

    override fun installedApps(): Single<List<AppInfo>> {
        return packageManager.queryIntentActivities(launcherIntent(), 0)
            .map {
                AppInfo(
                    name = it.activityInfo.applicationInfo.loadLabel(packageManager).toString(),
                    packageName = it.activityInfo.packageName,
                    installed = true
                )
            }
            .toSingle()
    }

    override fun appInfo(packageName: String): Single<AppInfo> {
        return Single.fromCallable {
            packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        }.map {
            AppInfo(
                name = it.loadLabel(packageManager).toString(),
                packageName = it.packageName,
                installed = true
            )
        }.onErrorResumeNext {
            Single.just(
                AppInfo(
                    name = "",
                    packageName = packageName,
                    installed = false
                )
            )
        }
    }

    fun launcherIntent(): Intent {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        return intent
    }

}
