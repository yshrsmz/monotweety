package net.yslibrary.monotweety.data.status.local


import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.status.Tweet
import javax.inject.Inject

@UserScope
class StatusLocalRepositoryImpl @Inject constructor() : StatusLocalRepository {

  val previousTweetSubject: BehaviorSubject<Optional<Tweet>> = BehaviorSubject.createDefault(None)

  override fun clear(): Completable {
    return Completable.fromAction {
      previousTweetSubject.onNext(None)
    }
  }

  override fun getPrevious(): Observable<Optional<Tweet>> {
    return previousTweetSubject
  }

  override fun update(tweet: Tweet): Completable {
    return Completable.fromAction {
      previousTweetSubject.onNext(tweet.toOptional())
    }
  }
}