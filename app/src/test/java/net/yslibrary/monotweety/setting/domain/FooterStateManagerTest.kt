package net.yslibrary.monotweety.setting.domain

import net.yslibrary.monotweety.ConfiguredRobolectricTestRunner
import net.yslibrary.monotweety.data.setting.SettingDataManager
import net.yslibrary.monotweety.data.setting.SettingModule
import net.yslibrary.monotweety.spy
import net.yslibrary.monotweety.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import rx.observers.TestSubscriber

/**
 * Created by yshrsmz on 2016/11/06.
 */
@RunWith(ConfiguredRobolectricTestRunner::class)
class FooterStateManagerTest {

  lateinit var settingDataManager: SettingDataManager

  lateinit var footerStateManager: FooterStateManager

  lateinit var ts: TestSubscriber<FooterStateManager.FooterState>

  @Before
  fun setup() {
    val module = SettingModule()
    settingDataManager = spy(module.provideSettingDataManager(module.provideSettingPreferences(RuntimeEnvironment.application)))

    footerStateManager = FooterStateManager(settingDataManager)

    ts = TestSubscriber.create<FooterStateManager.FooterState>()
  }

  @Test
  fun getAndSet() {
    footerStateManager.get()
        .subscribe(ts)

    verify(settingDataManager).footerEnabled()
    verify(settingDataManager).footerText()

    ts.assertNotCompleted()
    ts.assertValue(FooterStateManager.FooterState(false, ""))

    footerStateManager.set(FooterStateManager.FooterState(true, "this_is_footer"))

    verify(settingDataManager).footerEnabled(true)
    verify(settingDataManager).footerText("this_is_footer")

    ts.assertValues(FooterStateManager.FooterState(false, ""), FooterStateManager.FooterState(true, "this_is_footer"))
    ts.assertNotCompleted()
  }
}