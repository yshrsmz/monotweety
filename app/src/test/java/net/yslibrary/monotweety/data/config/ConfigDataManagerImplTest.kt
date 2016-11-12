package net.yslibrary.monotweety.data.config

import com.f2prateek.rx.preferences.RxSharedPreferences
import com.google.gson.Gson
import com.twitter.sdk.android.core.models.Configuration
import net.yslibrary.monotweety.*
import net.yslibrary.monotweety.base.Clock
import net.yslibrary.monotweety.data.config.local.ConfigLocalDataManager
import net.yslibrary.monotweety.data.config.local.ConfigLocalDataManagerImpl
import net.yslibrary.monotweety.data.config.remote.ConfigRemoteDataManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import rx.Single
import rx.observers.TestSubscriber

/**
 * Created by yshrsmz on 2016/11/12.
 */
@RunWith(ConfiguredRobolectricTestRunner::class)
class ConfigDataManagerImplTest {

  lateinit var configDataManager: ConfigDataManagerImpl
  lateinit var prefs: RxSharedPreferences
  lateinit var configLocalDataManager: ConfigLocalDataManager
  lateinit var configRemoteDataManager: ConfigRemoteDataManager
  lateinit var clock: Clock

  lateinit var ts: TestSubscriber<Int>

  val gson = Gson()
  val config = gson.fromJson(readJsonFromAssets("configuration.json"), Configuration::class.java)

  @Before
  fun setup() {
    val module = ConfigModule()
    clock = mock(Clock::class)
    prefs = module.provideConfigPreferences(RuntimeEnvironment.application)
    configLocalDataManager = spy(module.provideConfigLocalDataManager(prefs, clock))
    configRemoteDataManager = mock(ConfigRemoteDataManager::class)
    configDataManager = spy(module.provideConfigDataManager(configRemoteDataManager, configLocalDataManager, clock) as ConfigDataManagerImpl)

    ts = TestSubscriber.create()
  }

  @Test
  fun shortUrlLengthHttps_outdated() {
    val time = System.currentTimeMillis()
    whenever(configRemoteDataManager.get())
        .thenReturn(Single.just(config))

    whenever(clock.currentTimeMillis())
        .thenReturn(time)

    prefs.getInteger(ConfigLocalDataManagerImpl.SHORT_URL_LENGTH_HTTPS).set(22)

    configDataManager.shortUrlLengthHttps()
        .subscribe(ts)

    ts.assertValue(22)
    ts.assertNotCompleted()

    verify(configLocalDataManager).shortUrlLengthHttps()
    verify(configLocalDataManager).updatedAt()
    verify(configLocalDataManager).outdated()

    verify(configRemoteDataManager).get()
    verify(configLocalDataManager).shortUrlLengthHttps(23)
    verify(configLocalDataManager).updatedAt(time)

    verify(configDataManager).updateConfig(config)

    verifyNoMoreInteractions(configLocalDataManager, configRemoteDataManager)
  }

  @Test
  fun shortUrlLengthHttps_updated() {
    val time = System.currentTimeMillis()
    prefs.getInteger(ConfigLocalDataManagerImpl.SHORT_URL_LENGTH_HTTPS).set(22)
    prefs.getLong(ConfigLocalDataManagerImpl.UPDATED_AT).set(time)

    configDataManager.shortUrlLengthHttps().subscribe(ts)

    ts.assertValue(22)
    ts.assertNotCompleted()

    verify(configLocalDataManager).shortUrlLengthHttps()
    verify(configLocalDataManager).updatedAt()
    verify(configLocalDataManager).outdated()

    verifyNoMoreInteractions(configLocalDataManager, configRemoteDataManager)
  }
}