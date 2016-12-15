package net.yslibrary.monotweety.data.status.local

import com.google.gson.Gson
import com.twitter.sdk.android.core.models.Tweet
import net.yslibrary.monotweety.assertThat
import net.yslibrary.monotweety.readJsonFromAssets
import org.junit.Before
import org.junit.Test
import rx.observers.TestSubscriber

/**
 * Created by yshrsmz on 2016/12/15.
 */
class StatusLocalRepositoryImplTest {

  lateinit var repository: StatusLocalRepositoryImpl

  val gson = Gson()
  val tweet = gson.fromJson(readJsonFromAssets("tweet.json"), Tweet::class.java)
  val tweet2 = gson.fromJson(readJsonFromAssets("tweet_2.json"), Tweet::class.java)

  @Before
  fun setup() {
    repository = StatusLocalRepositoryImpl()
  }

  @Test
  fun clear() {
    val ts = TestSubscriber<Unit>()
    repository.previousTweetSubject.onNext(tweet)

    repository.clear().subscribe(ts)

    ts.assertNoValues()
    ts.assertCompleted()

    assertThat(repository.previousTweetSubject.hasValue()).isTrue()
    assertThat(repository.previousTweetSubject.value).isNull()
  }

  @Test
  fun getPrevious() {
    val ts = TestSubscriber<Tweet>()
    repository.previousTweetSubject.onNext(tweet)

    repository.getPrevious().subscribe(ts)

    ts.assertValue(tweet)
    ts.assertNotCompleted()

    assertThat(repository.previousTweetSubject.value).isEqualTo(tweet)
  }

  @Test
  fun update() {
    val ts = TestSubscriber<Unit>()
    repository.previousTweetSubject.onNext(tweet)

    repository.update(tweet2).subscribe(ts)

    ts.assertNoValues()
    ts.assertCompleted()

    assertThat(repository.previousTweetSubject.value).isEqualTo(tweet2)
  }
}