package net.yslibrary.monotweety.data.status.remote

import com.google.gson.Gson
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.models.Tweet
import net.yslibrary.monotweety.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import retrofit2.Call
import rx.observers.TestSubscriber
import java.net.URLEncoder

/**
 * Created by yshrsmz on 2017/01/07.
 */
@RunWith(ConfiguredRobolectricTestRunner::class)
class StatusRemoteRepositoryImplTest {

  lateinit var mockService: UpdateStatusService
  lateinit var mockCall: Call<Tweet>
  lateinit var callbackCaptor: ArgumentCaptor<Callback<Tweet>>
  lateinit var repository: StatusRemoteRepositoryImpl

  lateinit var ts: TestSubscriber<Tweet>

  val gson = Gson()

  @Suppress("UNCHECKED_CAST")
  @Before
  fun setup() {
    mockService = mock(UpdateStatusService::class)
    mockCall = mock(Call::class) as Call<Tweet>
    callbackCaptor = ArgumentCaptor.forClass(Callback::class.java) as ArgumentCaptor<Callback<Tweet>>
    ts = TestSubscriber()

    repository = StatusRemoteRepositoryImpl(mockService)
  }

  @Test
  fun update() {
    val tweet = gson.fromJson(readJsonFromAssets("tweet.json"), Tweet::class.java)
    whenever(mockService.update(anyString(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
        .thenReturn(mockCall)

    repository.update("this is test string").subscribe(ts)

    verify(mockService).update(URLEncoder.encode("this is test string", "UTF-8"), null, null, null, null, null, null, null, null)
    verify(mockCall).enqueue(callbackCaptor.capture())
    callbackCaptor.value.success(Result(tweet, null))

    ts.assertValue(tweet)
    ts.assertCompleted()
  }

  @Test
  fun update_inreplyto() {
    val tweet = gson.fromJson(readJsonFromAssets("tweet.json"), Tweet::class.java)
    whenever(mockService.update(anyString(), anyLong(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
        .thenReturn(mockCall)

    repository.update("this is test string", 12345).subscribe(ts)

    verify(mockService).update(URLEncoder.encode("this is test string", "UTF-8"), 12345, null, null, null, null, null, null, null)
    verify(mockCall).enqueue(callbackCaptor.capture())
    callbackCaptor.value.success(Result(tweet, null))

    ts.assertValue(tweet)
    ts.assertCompleted()
  }

  @Test
  fun update_asterisk() {
    val tweet = gson.fromJson(readJsonFromAssets("tweet.json"), Tweet::class.java)
    whenever(mockService.update(anyString(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
        .thenReturn(mockCall)

    repository.update("**").subscribe(ts)

    verify(mockService).update("%2A%2A", null, null, null, null, null, null, null, null)
    verify(mockCall).enqueue(callbackCaptor.capture())
    callbackCaptor.value.success(Result(tweet, null))

    ts.assertValue(tweet)
    ts.assertCompleted()
  }
}