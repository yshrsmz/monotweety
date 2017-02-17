package net.yslibrary.monotweety.data.status.local


import net.yslibrary.monotweety.data.status.Tweet
import rx.Completable
import rx.Observable

/**
 * Created by yshrsmz on 2016/10/02.
 */
interface StatusLocalRepository {
  fun update(tweet: Tweet): Completable
  fun clear(): Completable
  fun getPrevious(): Observable<Tweet?>
}