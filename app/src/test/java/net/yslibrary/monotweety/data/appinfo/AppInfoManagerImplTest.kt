package net.yslibrary.monotweety.data.appinfo

import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import net.yslibrary.monotweety.ConfiguredRobolectricTestRunner
import net.yslibrary.monotweety.assertThat
import net.yslibrary.monotweety.newPackageInfo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.res.builder.RobolectricPackageManager
import org.robolectric.shadows.ShadowResolveInfo
import rx.observers.TestSubscriber

@RunWith(ConfiguredRobolectricTestRunner::class)
class AppInfoManagerImplTest {

  lateinit var packageManager: RobolectricPackageManager

  lateinit var appInfoManager: AppInfoManagerImpl

  @Before
  fun setup() {
    packageManager = RuntimeEnvironment.getRobolectricPackageManager()

    appInfoManager = AppInfoManagerImpl(packageManager as PackageManager)
  }

  @After
  fun tearDown() {
    packageManager.reset()
  }

  @Test
  fun isInstalled() {
    packageManager.addPackage(newPackageInfo("Fenix", TwitterApp.FENIX.packageName))

    appInfoManager.isInstalled(TwitterApp.FENIX.packageName)
        .test()
        .awaitTerminalEvent()
        .assertValue(true)
        .assertCompleted()
  }

  @Test
  fun isInstalled_false() {
    packageManager.addPackage(newPackageInfo("Fenix", TwitterApp.FENIX.packageName))

    appInfoManager.isInstalled(TwitterApp.BEETER.packageName)
        .test()
        .awaitTerminalEvent()
        .assertValue(false)
        .assertCompleted()
  }

  @Test
  fun installedApps() {
    val infoList = listOf<ResolveInfo>(
        ShadowResolveInfo.newResolveInfo("Fenix", TwitterApp.FENIX.packageName),
        ShadowResolveInfo.newResolveInfo("beeter", TwitterApp.BEETER.packageName)
    )
    val ts = TestSubscriber<List<AppInfo>>()

    packageManager.addResolveInfoForIntent(appInfoManager.launcherIntent(), infoList)

    appInfoManager.installedApps().subscribe(ts)

    ts.assertValueCount(1)
    ts.assertCompleted()
    val result = ts.onNextEvents[0]

    assertThat(result).hasSize(2)
    assertThat(result[0].packageName).isEqualTo(TwitterApp.FENIX.packageName)
    assertThat(result[1].packageName).isEqualTo(TwitterApp.BEETER.packageName)
  }

  @Test
  fun appInfo_installed() {
    val ts = TestSubscriber<AppInfo>()
    packageManager.addPackage(newPackageInfo("Fenix", TwitterApp.FENIX.packageName))

    appInfoManager.appInfo(TwitterApp.FENIX.packageName).subscribe(ts)

    ts.assertValueCount(1)
    ts.assertCompleted()

    val result = ts.onNextEvents[0]

    assertThat(result.installed).isTrue()
    assertThat(result.packageName).isEqualTo(TwitterApp.FENIX.packageName)
  }

  @Test
  fun appInfo_notinstalled() {
    val ts = TestSubscriber<AppInfo>()
    packageManager.addPackage(newPackageInfo("Fenix", TwitterApp.FENIX.packageName))

    appInfoManager.appInfo(TwitterApp.BEETER.packageName).subscribe(ts)

    ts.assertValueCount(1)
    ts.assertCompleted()

    val result = ts.onNextEvents[0]

    assertThat(result.installed).isFalse()
    assertThat(result.packageName).isEqualTo(TwitterApp.BEETER.packageName)
  }
}