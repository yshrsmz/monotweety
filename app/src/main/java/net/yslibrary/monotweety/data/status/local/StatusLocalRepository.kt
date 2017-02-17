package net.yslibrary.monotweety.data.status.local


import net.yslibrary.monotweety.data.status.Tweet
import rx.Completable
import rx.Observable

interface StatusLocalRepository {
  fun update(tweet: Tweet): Completable
  fun clear(): Completable
  fun getPrevious(): Observable<Tweet?>
}