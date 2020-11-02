package net.yslibrary.monotweety.data.appinfo

import android.content.Intent
import android.content.pm.PackageManager
import javax.inject.Inject

interface TwitterAppRepository {
    suspend fun getInstalledApps(): List<AppInfo>
}

internal class TwitterAppRepositoryImpl @Inject constructor(
    private val packageManager: PackageManager,
) : TwitterAppRepository {
    private val twitterApps = TwitterApp.packages()

    override suspend fun getInstalledApps(): List<AppInfo> {
        return packageManager.queryIntentActivities(launcherIntent(), 0)
            .mapNotNull { resolveInfo ->
                val label = resolveInfo.loadLabel(packageManager)
                val packageName =
                    twitterApps.firstOrNull { it == resolveInfo.activityInfo.packageName }
                if (packageName != null) {
                    AppInfo(
                        name = label.toString(),
                        packageName = TwitterApp.fromPackageName(packageName)
                    )
                } else null
            }
    }

    private fun launcherIntent(): Intent {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        return intent
    }
}
