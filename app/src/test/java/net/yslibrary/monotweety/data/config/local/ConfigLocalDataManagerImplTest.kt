package net.yslibrary.monotweety.data.config.local

import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import net.yslibrary.monotweety.ConfiguredRobolectricTestRunner
import net.yslibrary.monotweety.base.Clock
import net.yslibrary.monotweety.data.config.ConfigModule
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

@RunWith(ConfiguredRobolectricTestRunner::class)
class ConfigLocalDataManagerImplTest {

  var mockClock: Clock by Delegates.notNull<Clock>()
  var rxPrefs: RxSharedPreferences by Delegates.notNull<RxSharedPreferences>()
  var manager: ConfigLocalDataManagerImpl by Delegates.notNull<ConfigLocalDataManagerImpl>()

  @Before
  fun setup() {
    mockClock = mock<Clock>()
    val module = ConfigModule()
    rxPrefs = module.provideConfigPreferences(RuntimeEnvironment.application)
    manager = ConfigLocalDataManagerImpl(rxPrefs, mockClock)
  }

  @Test
  fun shortUrlLengthHttps_get() {

    val prefs = rxPrefs.getInteger(ConfigLocalDataManagerImpl.SHORT_URL_LENGTH_HTTPS)

    manager.shortUrlLengthHttps().test()
        .apply {
          prefs.set(24)

          assertValues(23, 24) // initial value
          assertNoErrors()
          assertNotComplete()
        }
  }

  @Test
  fun shortUrlLengthHttps_set() {
    val time = System.currentTimeMillis()
    whenever(mockClock.currentTimeMillis()).thenReturn(time)

    val lengthPrefs = rxPrefs.getInteger(ConfigLocalDataManagerImpl.SHORT_URL_LENGTH_HTTPS)

    manager.shortUrlLengthHttps(24)

    Assertions.assertThat(lengthPrefs.get()).isEqualTo(24)
  }

  @Test
  fun updatedAt_get() {
    val prefs = rxPrefs.getLong(ConfigLocalDataManagerImpl.UPDATED_AT)

    Assertions.assertThat(manager.updatedAt()).isEqualTo(0L)

    prefs.set(12345L)

    Assertions.assertThat(manager.updatedAt()).isEqualTo(12345L)
  }

  @Test
  fun updatedAt_set() {
    val time = System.currentTimeMillis()

    val updatedAtPrefs = rxPrefs.getLong(ConfigLocalDataManagerImpl.UPDATED_AT)

    manager.updatedAt(time)

    Assertions.assertThat(updatedAtPrefs.get()).isEqualTo(time)
  }

  @Test
  fun outdated_in_time() {
    val time = System.currentTimeMillis()
    val pref = rxPrefs.getLong(ConfigLocalDataManagerImpl.UPDATED_AT)
    whenever(mockClock.currentTimeMillis()).thenReturn(time)

    pref.set(time)

    Assertions.assertThat(manager.outdated()).isFalse()
  }

  @Test
  fun outdated_outdated() {
    val time = System.currentTimeMillis()
    val outdated = time - TimeUnit.HOURS.toMillis(12)
    val pref = rxPrefs.getLong(ConfigLocalDataManagerImpl.UPDATED_AT)
    whenever(mockClock.currentTimeMillis()).thenReturn(time)

    pref.set(outdated)

    Assertions.assertThat(manager.outdated()).isTrue()
  }
}