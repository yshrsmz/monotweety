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
class NotificationEnabledManagerTest {

    lateinit var settingDataManager: SettingDataManager
    lateinit var notificationEnabledManager: NotificationEnabledManager

    @Before
    fun setup() {
        val module = SettingModule()
        settingDataManager =
            spyk(module.provideSettingDataManager(module.provideSettingPreferences(targetApplication)))
        notificationEnabledManager = NotificationEnabledManager(settingDataManager)
    }

    @Test
    fun getAndSet() {
        notificationEnabledManager.get().test()
            .apply {
                notificationEnabledManager.set(true)


                verify {
                    settingDataManager.notificationEnabled()
                    settingDataManager.notificationEnabled(true)
                }
                confirmVerified(settingDataManager)

                assertValues(false, true)
                assertNoErrors()
                assertNotComplete()
            }
    }
}
