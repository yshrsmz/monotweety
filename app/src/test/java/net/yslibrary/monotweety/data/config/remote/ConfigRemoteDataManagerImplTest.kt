package net.yslibrary.monotweety.data.config.remote

import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Configuration
import com.twitter.sdk.android.core.services.ConfigurationService
import net.yslibrary.monotweety.ConfiguredRobolectricTestRunner
import net.yslibrary.monotweety.readJsonFromAssets
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import retrofit2.Call
import rx.observers.TestSubscriber


/**
 * Created by yshrsmz on 2016/10/20.
 */
@RunWith(ConfiguredRobolectricTestRunner::class)
class ConfigRemoteDataManagerImplTest {

  lateinit var manager: ConfigRemoteDataManagerImpl
  lateinit var configService: ConfigurationService
  lateinit var mockCall: Call<Configuration>
  lateinit var callbackCaptor: ArgumentCaptor<Callback<Configuration>>

  lateinit var ts: TestSubscriber<Configuration>

  val gson = Gson()

  @Suppress("UNCHECKED_CAST")
  @Before
  fun setup() {
    configService = mock<ConfigurationService>()
    mockCall = mock<Call<Configuration>>()
    callbackCaptor = ArgumentCaptor.forClass(Callback::class.java) as ArgumentCaptor<Callback<Configuration>>

    manager = ConfigRemoteDataManagerImpl(configService)

    ts = TestSubscriber.create()
  }

  @Test
  fun get() {
    val resString = readJsonFromAssets("configuration.json")
    val config = gson.fromJson(resString, Configuration::class.java)
    whenever(configService.configuration()).thenReturn(mockCall)

    manager.get().subscribe(ts)

    verify(mockCall).enqueue(callbackCaptor.capture())
    callbackCaptor.value.success(Result(config, null))

    ts.assertValue(config)
    ts.assertCompleted()
  }

  @Test
  fun get_error() {
    val e = TwitterException("Request Failure", NullPointerException())
    whenever(configService.configuration()).thenReturn(mockCall)

    manager.get().subscribe(ts)

    verify(mockCall).enqueue(callbackCaptor.capture())
    callbackCaptor.value.failure(e)

    ts.assertNoValues()
    ts.assertError(e)
  }

  @Test
  fun get_unsubscribe() {
    whenever(configService.configuration()).thenReturn(mockCall)

    val s = manager.get().subscribe(ts)
    verify(mockCall).enqueue(callbackCaptor.capture())

    s.unsubscribe()
    verify(mockCall).cancel()

    ts.assertNoValues()
    ts.assertUnsubscribed()
  }
}