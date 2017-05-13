package net.yslibrary.monotweety.data.status.local


import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.status.Tweet
import rx.Completable
import rx.Observable
import rx.subjects.BehaviorSubject
import javax.inject.Inject

@UserScope
class StatusLocalRepositoryImpl @Inject constructor() : StatusLocalRepository {

  val previousTweetSubject: BehaviorSubject<Tweet?> = BehaviorSubject.create(null as Tweet?)

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