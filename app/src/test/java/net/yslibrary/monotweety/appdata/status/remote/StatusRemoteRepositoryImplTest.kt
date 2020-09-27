package net.yslibrary.monotweety.data.status.remote

import com.google.gson.Gson
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import io.mockk.CapturingSlot
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.verify
import net.yslibrary.monotweety.data.status.Tweet
import net.yslibrary.monotweety.readJsonFromAssets
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import java.net.URLEncoder
import com.twitter.sdk.android.core.models.Tweet as TwitterTweet

class StatusRemoteRepositoryImplTest {

    @MockK
    lateinit var mockService: UpdateStatusService
    @MockK
    lateinit var mockCall: Call<TwitterTweet>

    lateinit var callbackCaptor: CapturingSlot<Callback<TwitterTweet>>
    lateinit var repository: StatusRemoteRepositoryImpl

    val gson = Gson()

    @Suppress("UNCHECKED_CAST")
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        callbackCaptor = slot<Callback<TwitterTweet>>()

        repository = StatusRemoteRepositoryImpl(mockService)
    }

    @Test
    fun update() {
        val tweet = gson.fromJson(readJsonFromAssets("tweet.json"), TwitterTweet::class.java)
        val result = Tweet.from(tweet)

        every {
            mockService.update(any(), any(), any(), any(), any(), any(), any(), any(), any())
        } returns mockCall

        every {
            mockCall.enqueue(capture(callbackCaptor))
        } returns Unit

        repository.update("this is test string").test()
            .apply {
                verify {
                    mockService.update(
                        URLEncoder.encode("this is test string", "UTF-8"),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )

                    mockCall.enqueue(callbackCaptor.captured)
                }

                confirmVerified(mockService, mockCall)

                callbackCaptor.captured.success(Result(tweet, null))

                assertNoErrors()
                assertValue(result)
                assertComplete()
            }
    }

    @Test
    fun update_inreplyto() {
        val tweet = gson.fromJson(readJsonFromAssets("tweet.json"), TwitterTweet::class.java)
        val result = Tweet.from(tweet)

        every {
            mockService.update(any(), any(), any(), any(), any(), any(), any(), any(), any())
        } returns mockCall

        every {
            mockCall.enqueue(capture(callbackCaptor))
        } returns Unit

        repository.update("this is test string", 12345).test()
            .apply {

                verify {
                    mockService.update(
                        URLEncoder.encode("this is test string", "UTF-8"),
                        12345,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )

                    mockCall.enqueue(callbackCaptor.captured)
                }

                confirmVerified(mockService, mockCall)

                callbackCaptor.captured.success(Result(tweet, null))

                assertNoErrors()
                assertValue(result)
                assertComplete()
            }
    }

    @Test
    fun update_asterisk() {
        val tweet = gson.fromJson(readJsonFromAssets("tweet.json"), TwitterTweet::class.java)
        val result = Tweet.from(tweet)

        every {
            mockService.update(any(), any(), any(), any(), any(), any(), any(), any(), any())
        } returns mockCall

        every {
            mockCall.enqueue(capture(callbackCaptor))
        } returns Unit

        repository.update("**").test()
            .apply {

                verify {
                    mockService.update("%2A%2A", null, null, null, null, null, null, null, null)

                    mockCall.enqueue(callbackCaptor.captured)
                }

                confirmVerified(mockService, mockCall)

                callbackCaptor.captured.success(Result(tweet, null))

                assertNoErrors()
                assertValue(result)
                assertComplete()
            }
    }
}
