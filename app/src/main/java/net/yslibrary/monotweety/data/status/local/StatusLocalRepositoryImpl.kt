package net.yslibrary.monotweety.data.status.local

import com.twitter.sdk.android.core.models.Tweet
import rx.Completable
import rx.Observable
import rx.lang.kotlin.BehaviorSubject

/**
 * Created by yshrsmz on 2016/10/02.
 */
class StatusLocalRepositoryImpl : StatusLocalRepository {

  val previousTweetSubject = BehaviorSubject<Tweet?>(null)

  override fun clear(): Completable {
    return Completable.fromAction {
      previousTweetSubject.onNext(null)
    }
  }

  override fun getPrevious(): Observable<Tweet?> {
    return previousTweetSubject.asObservable()
  }

  override fun update(tweet: Tweet): Completable {
    return Completable.fromAction {
      previousTweetSubject.onNext(tweet)
    }
  }
}