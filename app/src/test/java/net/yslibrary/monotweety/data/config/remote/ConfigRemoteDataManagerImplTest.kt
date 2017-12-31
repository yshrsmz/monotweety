package net.yslibrary.monotweety.data.config.remote

import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.services.ConfigurationService
import net.yslibrary.monotweety.ConfiguredRobolectricTestRunner
import net.yslibrary.monotweety.assertThat
import net.yslibrary.monotweety.data.config.Configuration
import net.yslibrary.monotweety.readJsonFromAssets
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import retrofit2.Call
import com.twitter.sdk.android.core.models.Configuration as TwitterConfig

@RunWith(ConfiguredRobolectricTestRunner::class)
class ConfigRemoteDataManagerImplTest {

  lateinit var manager: ConfigRemoteDataManagerImpl
  lateinit var configService: ConfigurationService
  lateinit var mockCall: Call<TwitterConfig>
  lateinit var callbackCaptor: ArgumentCaptor<Callback<TwitterConfig>>

  val gson = Gson()

  @Suppress("UNCHECKED_CAST")
  @Before
  fun setup() {
    configService = mock<ConfigurationService>()
    mockCall = mock<Call<TwitterConfig>>()
    callbackCaptor = ArgumentCaptor.forClass(Callback::class.java) as ArgumentCaptor<Callback<TwitterConfig>>

    manager = ConfigRemoteDataManagerImpl(configService)
  }

  @Test
  fun get() {
    val resString = readJsonFromAssets("configuration.json")
    val config = gson.fromJson(resString, TwitterConfig::class.java)
    val result = Configuration.from(config)

    whenever(configService.configuration()).thenReturn(mockCall)

    manager.get().test()
        .apply {
          verify(mockCall).enqueue(callbackCaptor.capture())
          callbackCaptor.value.success(Result(config, null))

          assertValue(result)
          assertComplete()
        }
  }

  @Test
  fun get_error() {
    val e = TwitterException("Request Failure", NullPointerException())
    whenever(configService.configuration()).thenReturn(mockCall)

    manager.get().test()
        .apply {
          verify(mockCall).enqueue(callbackCaptor.capture())
          callbackCaptor.value.failure(e)

          assertNoValues()
          assertError(e)
        }
  }

  @Test
  fun get_unsubscribe() {
    whenever(configService.configuration()).thenReturn(mockCall)

    manager.get().test()
        .apply {
          verify(mockCall).enqueue(callbackCaptor.capture())

          dispose()
          verify(mockCall).cancel()

          assertNoValues()
          assertThat(isDisposed).isTrue()
        }
  }
}