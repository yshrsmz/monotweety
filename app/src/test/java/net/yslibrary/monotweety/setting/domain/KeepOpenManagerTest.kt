package net.yslibrary.monotweety.setting.domain

import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import net.yslibrary.monotweety.ConfiguredRobolectricTestRunner
import net.yslibrary.monotweety.data.setting.SettingDataManager
import net.yslibrary.monotweety.data.setting.SettingModule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment

@RunWith(ConfiguredRobolectricTestRunner::class)
class KeepOpenManagerTest {

  lateinit var settingDataManager: SettingDataManager

  lateinit var keepOpenManager: KeepOpenManager

  @Before
  fun setup() {
    val module = SettingModule()
    settingDataManager = spy(module.provideSettingDataManager(module.provideSettingPreferences(RuntimeEnvironment.application)))
    keepOpenManager = KeepOpenManager(settingDataManager)
  }

  @Test
  fun getAndSet() {
    keepOpenManager.get().test()
        .apply {
          keepOpenManager.set(true)

          verify(settingDataManager).keepOpen()
          verify(settingDataManager).keepOpen(true)
          verifyNoMoreInteractions(settingDataManager)

          assertValues(false, true)
          assertNoErrors()
          assertNotComplete()
        }
  }
}