package net.yslibrary.monotweety.setting.domain

import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import net.yslibrary.monotweety.ConfiguredRobolectricTestRunner
import net.yslibrary.monotweety.data.setting.SettingDataManager
import net.yslibrary.monotweety.data.setting.SettingModule
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

  lateinit var ts: TestSubscriber<FooterStateManager.State>

  @Before
  fun setup() {
    val module = SettingModule()
    settingDataManager = spy(module.provideSettingDataManager(module.provideSettingPreferences(RuntimeEnvironment.application)))

    footerStateManager = FooterStateManager(settingDataManager)

    ts = TestSubscriber.create<FooterStateManager.State>()
  }

  @Test
  fun getAndSet() {
    footerStateManager.get()
        .subscribe(ts)

    verify(settingDataManager).footerEnabled()
    verify(settingDataManager).footerText()

    ts.assertNotCompleted()
    ts.assertValue(FooterStateManager.State(false, ""))

    footerStateManager.set(FooterStateManager.State(true, "this_is_footer"))

    verify(settingDataManager).footerEnabled(true)
    verify(settingDataManager).footerText("this_is_footer")

    ts.assertValues(FooterStateManager.State(false, ""), FooterStateManager.State(true, "this_is_footer"))
    ts.assertNotCompleted()
  }
}