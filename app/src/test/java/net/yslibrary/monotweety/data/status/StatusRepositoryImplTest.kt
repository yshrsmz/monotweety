package net.yslibrary.monotweety.data.status

import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import net.yslibrary.monotweety.data.status.remote.StatusRemoteRepository
import net.yslibrary.monotweety.readJsonFromAssets
import org.junit.Before
import org.junit.Test
import com.twitter.sdk.android.core.models.Tweet as TwitterTweet

class StatusRepositoryImplTest {


    lateinit var repository: StatusRepositoryImpl
    lateinit var mockRemoteRepository: StatusRemoteRepository

    val gson = Gson()

    @Before
    fun setup() {
        mockRemoteRepository = mock<StatusRemoteRepository>()

        repository = StatusRepositoryImpl(mockRemoteRepository)
    }

    @Test
    fun updateStatus() {
        val tweet = Tweet.from(gson.fromJson(readJsonFromAssets("tweet.json"), TwitterTweet::class.java))
        whenever(mockRemoteRepository.update(any(), anyOrNull())).thenReturn(Single.just(tweet))

        repository.updateStatus("test status").test()
            .run {
                awaitTerminalEvent()

                assertNoValues()
                assertComplete()

                verify(mockRemoteRepository).update("test status", null)
            }
    }
}
