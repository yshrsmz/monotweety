package net.yslibrary.monotweety.data.status.remote

import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.*
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import net.yslibrary.monotweety.ConfiguredRobolectricTestRunner
import net.yslibrary.monotweety.data.status.Tweet
import net.yslibrary.monotweety.readJsonFromAssets
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import retrofit2.Call
import java.net.URLEncoder
import com.twitter.sdk.android.core.models.Tweet as TwitterTweet

@RunWith(ConfiguredRobolectricTestRunner::class)
class StatusRemoteRepositoryImplTest {

  lateinit var mockService: UpdateStatusService
  lateinit var mockCall: Call<TwitterTweet>
  lateinit var callbackCaptor: ArgumentCaptor<Callback<TwitterTweet>>
  lateinit var repository: StatusRemoteRepositoryImpl

  val gson = Gson()

  @Suppress("UNCHECKED_CAST")
  @Before
  fun setup() {
    mockService = mock<UpdateStatusService>()
    mockCall = mock<Call<TwitterTweet>>()
    callbackCaptor = ArgumentCaptor.forClass(Callback::class.java) as ArgumentCaptor<Callback<TwitterTweet>>

    repository = StatusRemoteRepositoryImpl(mockService)
  }

  @Test
  fun update() {
    val tweet = gson.fromJson(readJsonFromAssets("tweet.json"), TwitterTweet::class.java)
    val result = Tweet.from(tweet)
    whenever(mockService.update(any(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull()))
        .thenReturn(mockCall)

    repository.update("this is test string").test()
        .apply {
          verify(mockService).update(URLEncoder.encode("this is test string", "UTF-8"), null, null, null, null, null, null, null, null)
          verify(mockCall).enqueue(callbackCaptor.capture())
          callbackCaptor.value.success(Result(tweet, null))

          assertValue(result)
          assertComplete()
        }
  }

  @Test
  fun update_inreplyto() {
    val tweet = gson.fromJson(readJsonFromAssets("tweet.json"), TwitterTweet::class.java)
    val result = Tweet.from(tweet)
    whenever(mockService.update(any(), any(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull()))
        .thenReturn(mockCall)

    repository.update("this is test string", 12345).test()
        .apply {
          verify(mockService).update(URLEncoder.encode("this is test string", "UTF-8"), 12345, null, null, null, null, null, null, null)
          verify(mockCall).enqueue(callbackCaptor.capture())
          callbackCaptor.value.success(Result(tweet, null))

          assertValue(result)
          assertComplete()
        }
  }

  @Test
  fun update_asterisk() {
    val tweet = gson.fromJson(readJsonFromAssets("tweet.json"), TwitterTweet::class.java)
    val result = Tweet.from(tweet)
    whenever(mockService.update(any(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull()))
        .thenReturn(mockCall)

    repository.update("**").test()
        .apply {
          verify(mockService).update("%2A%2A", null, null, null, null, null, null, null, null)
          verify(mockCall).enqueue(callbackCaptor.capture())
          callbackCaptor.value.success(Result(tweet, null))

          assertValue(result)
          assertComplete()
        }
  }
}