package net.yslibrary.monotweety.setting.domain

import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.verify
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
            spyk(module.provideSettingDataManager(module.provideSettingPreferences(targetApplication)))

        footerStateManager = FooterStateManager(settingDataManager)
    }

    @Test
    fun getAndSet() {
        footerStateManager.get().test()
            .apply {

                assertNotComplete()
                assertValue(FooterStateManager.State(false, ""))

                footerStateManager.set(FooterStateManager.State(true, "this_is_footer"))

                verify {
                    settingDataManager.footerEnabled()
                    settingDataManager.footerText()
                    settingDataManager.footerEnabled(true)
                    settingDataManager.footerText("this_is_footer")
                }
                confirmVerified(settingDataManager)

                assertValues(
                    FooterStateManager.State(false, ""),
                    FooterStateManager.State(true, "this_is_footer")
                )
                assertNotComplete()
            }
    }
}
