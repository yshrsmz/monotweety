package net.yslibrary.monotweety.data.appinfo

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import com.google.common.truth.Truth.assertThat
import net.yslibrary.monotweety.newPackageInfo
import net.yslibrary.monotweety.targetApplication
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowPackageManager


@RunWith(RobolectricTestRunner::class)
class AppInfoManagerImplTest {

    lateinit var application: Application
    lateinit var packageManager: ShadowPackageManager

    lateinit var appInfoManager: AppInfoManagerImpl

    @Before
    fun setup() {
        application = targetApplication
        packageManager = Shadows.shadowOf(application.packageManager)

        appInfoManager = AppInfoManagerImpl(application.packageManager)
    }

    @Test
    fun isInstalled() {
        packageManager.installPackage(newPackageInfo("Fenix", TwitterApp.FENIX.packageName))

        appInfoManager.isInstalled(TwitterApp.FENIX.packageName).test()
            .apply {
                awaitTerminalEvent()
                assertValue(true)
                assertComplete()
            }
    }

    @Test
    fun isInstalled_false() {
        packageManager.installPackage(newPackageInfo("Fenix", TwitterApp.FENIX.packageName))

        appInfoManager.isInstalled(TwitterApp.BEETER.packageName).test()
            .apply {
                awaitTerminalEvent()
                assertValue(false)
                assertComplete()
            }
    }

    @Test
    fun installedApps() {
        listOf(
            ComponentName(TwitterApp.FENIX.packageName, "Test"),
            ComponentName(TwitterApp.BEETER.packageName, "Test")
        ).forEach { cn ->
            packageManager.addActivityIfNotPresent(cn)
            packageManager.addIntentFilterForActivity(
                cn,
                IntentFilter(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_LAUNCHER) })
        }

        appInfoManager.installedApps().test()
            .apply {
                assertValueCount(1)
                assertComplete()

                val result = values().first()
//                assertThat(result).hasSize(2)
                assertThat(result).isNotEmpty()
                assertThat(result).isNotEmpty()
                assertThat(result[0].packageName).isEqualTo(TwitterApp.FENIX.packageName)
                assertThat(result[1].packageName).isEqualTo(TwitterApp.BEETER.packageName)
            }
    }

    @Test
    fun appInfo_installed() {
        packageManager.installPackage(newPackageInfo("Fenix", TwitterApp.FENIX.packageName))

        appInfoManager.appInfo(TwitterApp.FENIX.packageName).test()
            .apply {
                assertValueCount(1)
                assertComplete()

                val result = values().first()

                assertThat(result.installed).isTrue()
                assertThat(result.packageName).isEqualTo(TwitterApp.FENIX.packageName)
            }
    }

    @Test
    fun appInfo_notinstalled() {
        packageManager.installPackage(newPackageInfo("Fenix", TwitterApp.FENIX.packageName))

        appInfoManager.appInfo(TwitterApp.BEETER.packageName).test()
            .apply {
                assertValueCount(1)
                assertComplete()

                val result = values().first()

                assertThat(result.installed).isFalse()
                assertThat(result.packageName).isEqualTo(TwitterApp.BEETER.packageName)
            }
    }
}
