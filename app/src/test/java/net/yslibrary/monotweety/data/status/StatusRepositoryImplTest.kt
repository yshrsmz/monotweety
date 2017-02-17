package net.yslibrary.monotweety.data.status

import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.*
import net.yslibrary.monotweety.data.status.local.StatusLocalRepository
import net.yslibrary.monotweety.data.status.remote.StatusRemoteRepository
import net.yslibrary.monotweety.readJsonFromAssets
import org.junit.Before
import org.junit.Test
import rx.Completable
import rx.Emitter
import rx.Observable
import rx.Single
import java.util.concurrent.TimeUnit
import com.twitter.sdk.android.core.models.Tweet as TwitterTweet

/**
 * Created by yshrsmz on 2017/02/07.
 */
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

    repository.updateStatus("test status")
        .test()
        .awaitTerminalEvent()
        .run {
          assertNoValues()
          assertCompleted()

          verify(mockRemoteRepository).update("test status", null)
          verify(mockLocalRepository).update(tweet)
          verifyNoMoreInteractions(mockLocalRepository, mockRemoteRepository)
        }
  }

  @Test
  fun previousStatus() {
    val tweet = Tweet.from(gson.fromJson(readJsonFromAssets("tweet.json"), TwitterTweet::class.java))

    whenever(mockLocalRepository.getPrevious())
        .thenReturn(Observable.fromEmitter({ emitter ->
          emitter.onNext(null)
          emitter.onNext(tweet)
        }, Emitter.BackpressureMode.LATEST))

    repository.previousStatus()
        .test()
        .awaitValueCount(2, 50, TimeUnit.MILLISECONDS)
        .run {
          assertValues(null, tweet)
          assertNotCompleted()

          verify(mockLocalRepository).getPrevious()
          verifyNoMoreInteractions(mockLocalRepository, mockRemoteRepository)
        }
  }

  @Test
  fun clear() {
    whenever(mockLocalRepository.clear()).thenReturn(Completable.complete())

    repository.clear()
        .test()
        .awaitTerminalEvent()
        .run {
          assertNoValues()
          assertCompleted()

          verify(mockLocalRepository).clear()
          verifyNoMoreInteractions(mockLocalRepository, mockRemoteRepository)
        }
  }
}