package net.yslibrary.monotweety.data.status

import com.gojuno.koptional.None
import com.gojuno.koptional.toOptional
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import net.yslibrary.monotweety.data.status.local.StatusLocalRepository
import net.yslibrary.monotweety.data.status.remote.StatusRemoteRepository
import net.yslibrary.monotweety.readJsonFromAssets
import org.junit.Before
import org.junit.Test
import com.twitter.sdk.android.core.models.Tweet as TwitterTweet

class StatusRepositoryImplTest {


  lateinit var repository: StatusRepositoryImpl
  lateinit var mockLocalRepository: StatusLocalRepository
  lateinit var mockRemoteRepository: StatusRemoteRepository

  val gson = Gson()

  @Before
  fun setup() {
    mockLocalRepository = mock<StatusLocalRepository>()
    mockRemoteRepository = mock<StatusRemoteRepository>()

    repository = StatusRepositoryImpl(mockRemoteRepository, mockLocalRepository)
  }

  @Test
  fun updateStatus() {
    val tweet = Tweet.from(gson.fromJson(readJsonFromAssets("tweet.json"), TwitterTweet::class.java))
    whenever(mockRemoteRepository.update(any(), anyOrNull())).thenReturn(Single.just(tweet))
    whenever(mockLocalRepository.update(tweet)).thenReturn(Completable.complete())

    repository.updateStatus("test status").test()
        .run {
          awaitTerminalEvent()

          assertNoValues()
          assertComplete()

          verify(mockRemoteRepository).update("test status", null)
          verify(mockLocalRepository).update(tweet)
          verifyNoMoreInteractions(mockLocalRepository, mockRemoteRepository)
        }
  }

  @Test
  fun previousStatus() {
    val tweet = Tweet.from(gson.fromJson(readJsonFromAssets("tweet.json"), TwitterTweet::class.java))

    whenever(mockLocalRepository.getPrevious())
        .thenReturn(Observable.create({ emitter ->
          emitter.onNext(None)
          emitter.onNext(tweet.toOptional())
        }))

    repository.previousStatus()
        .test()
        .run {
          awaitCount(2)
          assertValues(None, tweet.toOptional())
          assertNotComplete()

          verify(mockLocalRepository).getPrevious()
          verifyNoMoreInteractions(mockLocalRepository, mockRemoteRepository)
        }
  }

  @Test
  fun clear() {
    whenever(mockLocalRepository.clear()).thenReturn(Completable.complete())

    repository.clear().test()
        .run {
          awaitTerminalEvent()
          assertNoValues()
          assertComplete()

          verify(mockLocalRepository).clear()
          verifyNoMoreInteractions(mockLocalRepository, mockRemoteRepository)
        }
  }
}