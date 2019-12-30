package net.yslibrary.monotweety.data.status

import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.Single
import net.yslibrary.monotweety.data.status.remote.StatusRemoteRepository
import net.yslibrary.monotweety.readJsonFromAssets
import org.junit.Before
import org.junit.Test
import com.twitter.sdk.android.core.models.Tweet as TwitterTweet

class StatusRepositoryImplTest {

    @MockK
    lateinit var mockRemoteRepository: StatusRemoteRepository

    lateinit var repository: StatusRepositoryImpl

    val gson = Gson()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        repository = StatusRepositoryImpl(mockRemoteRepository)
    }

    @Test
    fun updateStatus() {
        val tweet =
            Tweet.from(gson.fromJson(readJsonFromAssets("tweet.json"), TwitterTweet::class.java))

        every { mockRemoteRepository.update(any(), any()) } returns Single.just(tweet)

        repository.updateStatus("test status").test()
            .run {
                awaitTerminalEvent()

                assertNoValues()
                assertComplete()

                verify {
                    mockRemoteRepository.update("test status", null)
                }
                confirmVerified(mockRemoteRepository)
            }
    }
}
