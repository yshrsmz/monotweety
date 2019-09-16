package net.yslibrary.monotweety.setting.domain

import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import net.yslibrary.monotweety.data.setting.SettingDataManager
import net.yslibrary.monotweety.data.setting.SettingModule
import net.yslibrary.monotweety.targetApplication
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FooterStateManagerTest {

    lateinit var settingDataManager: SettingDataManager

    lateinit var footerStateManager: FooterStateManager

    @Before
    fun setup() {
        val module = SettingModule()
        settingDataManager =
            spy(module.provideSettingDataManager(module.provideSettingPreferences(targetApplication)))

        footerStateManager = FooterStateManager(settingDataManager)
    }

    @Test
    fun getAndSet() {
        footerStateManager.get().test()
            .apply {
                verify(settingDataManager).footerEnabled()
                verify(settingDataManager).footerText()

                assertNotComplete()
                assertValue(FooterStateManager.State(false, ""))

                footerStateManager.set(FooterStateManager.State(true, "this_is_footer"))

                verify(settingDataManager).footerEnabled(true)
                verify(settingDataManager).footerText("this_is_footer")

                assertValues(
                    FooterStateManager.State(false, ""),
                    FooterStateManager.State(true, "this_is_footer")
                )
                assertNotComplete()
            }
    }
}
