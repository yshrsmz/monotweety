package net.yslibrary.monotweety.data.twitterapp

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
        return packageManager.runCatching { getPackageInfo(activityInfo.packageName, 0) }
            .map { it.applicationInfo }
            .map { applicationInfo ->
                val name = applicationInfo.loadLabel(packageManager).toString()
                val twitterApp = TwitterApp.fromPackageName(applicationInfo.packageName)
                    .takeUnless { it == TwitterApp.NONE } ?: return@map null

                AppInfo(
                    name = name,
                    packageName = twitterApp,
                )
            }
            .getOrNull()
    }
}
