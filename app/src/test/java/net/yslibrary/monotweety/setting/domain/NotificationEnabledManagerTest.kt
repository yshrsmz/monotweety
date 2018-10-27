package net.yslibrary.monotweety.setting.domain

import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import net.yslibrary.monotweety.ConfiguredRobolectricTestRunner
import net.yslibrary.monotweety.data.setting.SettingDataManager
import net.yslibrary.monotweety.data.setting.SettingModule
import net.yslibrary.monotweety.targetApplication
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(ConfiguredRobolectricTestRunner::class)
class NotificationEnabledManagerTest {

    lateinit var settingDataManager: SettingDataManager
    lateinit var notificationEnabledManager: NotificationEnabledManager

    @Before
    fun setup() {
        val module = SettingModule()
        settingDataManager = spy(module.provideSettingDataManager(module.provideSettingPreferences(targetApplication)))
        notificationEnabledManager = NotificationEnabledManager(settingDataManager)
    }

    @Test
    fun getAndSet() {
        notificationEnabledManager.get().test()
            .apply {
                notificationEnabledManager.set(true)

                verify(settingDataManager).notificationEnabled()
                verify(settingDataManager).notificationEnabled(true)
                verifyNoMoreInteractions(settingDataManager)

                assertValues(false, true)
                assertNoErrors()
                assertNotComplete()
            }
    }
}
