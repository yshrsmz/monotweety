package net.yslibrary.monotweety.data.appinfo

import android.app.Application
import android.content.pm.ResolveInfo
import net.yslibrary.monotweety.assertThat
import net.yslibrary.monotweety.newPackageInfo
import net.yslibrary.monotweety.targetApplication
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowPackageManager
import org.robolectric.shadows.ShadowResolveInfo


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
        packageManager.addPackage(newPackageInfo("Fenix", TwitterApp.FENIX.packageName))

        appInfoManager.isInstalled(TwitterApp.FENIX.packageName).test()
            .apply {
                awaitTerminalEvent()
                assertValue(true)
                assertComplete()
            }
    }

    @Test
    fun isInstalled_false() {
        packageManager.addPackage(newPackageInfo("Fenix", TwitterApp.FENIX.packageName))

        appInfoManager.isInstalled(TwitterApp.BEETER.packageName).test()
            .apply {
                awaitTerminalEvent()
                assertValue(false)
                assertComplete()
            }
    }

    @Test
    fun installedApps() {
        val infoList = listOf<ResolveInfo>(
            ShadowResolveInfo.newResolveInfo("Fenix", TwitterApp.FENIX.packageName),
            ShadowResolveInfo.newResolveInfo("beeter", TwitterApp.BEETER.packageName)
        )

        packageManager.setResolveInfosForIntent(appInfoManager.launcherIntent(), infoList)

        appInfoManager.installedApps().test()
            .apply {
                assertValueCount(1)
                assertComplete()

                val result = values().first()
//                assertThat(result).hasSize(2)
                assertThat(result).isNotEmpty
                assertThat(result[0].packageName).isEqualTo(TwitterApp.FENIX.packageName)
                assertThat(result[1].packageName).isEqualTo(TwitterApp.BEETER.packageName)
            }
    }

    @Test
    fun appInfo_installed() {
        packageManager.addPackage(newPackageInfo("Fenix", TwitterApp.FENIX.packageName))

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
        packageManager.addPackage(newPackageInfo("Fenix", TwitterApp.FENIX.packageName))

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
