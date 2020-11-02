package net.yslibrary.monotweety.data.appinfo

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import javax.inject.Inject

interface TwitterAppRepository {
    suspend fun getByPackageName(twitterApp: TwitterApp): AppInfo?
    suspend fun getInstalledApps(): List<AppInfo>
}

internal class TwitterAppRepositoryImpl @Inject constructor(
    private val packageManager: PackageManager,
) : TwitterAppRepository {
    private val twitterApps = TwitterApp.packages()

    override suspend fun getByPackageName(twitterApp: TwitterApp): AppInfo? {
        val intent = packageManager.getLaunchIntentForPackage(twitterApp.packageName) ?: return null
        return packageManager.queryIntentActivities(intent, 0)
            .map { resolveInfo -> resolveInfo.toAppInfo() }
            .firstOrNull()
    }

    override suspend fun getInstalledApps(): List<AppInfo> {
        return packageManager.queryIntentActivities(launcherIntent(), 0)
            .mapNotNull { resolveInfo -> resolveInfo.toAppInfo() }
    }

    private fun launcherIntent(): Intent {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        return intent
    }

    private fun ResolveInfo.toAppInfo(): AppInfo? {
        val label = loadLabel(packageManager)
        val packageName = twitterApps.firstOrNull { it == activityInfo.packageName } ?: return null
        val twitterApp = TwitterApp.fromPackageName(packageName)
            .takeUnless { it == TwitterApp.NONE } ?: return null

        return AppInfo(
            name = label.toString(),
            packageName = twitterApp
        )
    }
}
