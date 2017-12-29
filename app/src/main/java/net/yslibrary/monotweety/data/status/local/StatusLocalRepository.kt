package net.yslibrary.monotweety.data.status.local


import com.gojuno.koptional.Optional
import io.reactivex.Completable
import io.reactivex.Observable
import net.yslibrary.monotweety.data.status.Tweet

interface StatusLocalRepository {
  fun update(tweet: Tweet): Completable
  fun clear(): Completable
  fun getPrevious(): Observable<Optional<Tweet>>
}