package net.yslibrary.monotweety.data.status.local

import com.gojuno.koptional.None
import com.gojuno.koptional.toOptional
import com.google.gson.Gson
import net.yslibrary.monotweety.assertThat
import net.yslibrary.monotweety.data.status.Tweet
import net.yslibrary.monotweety.readJsonFromAssets
import org.junit.Before
import org.junit.Test
import com.twitter.sdk.android.core.models.Tweet as TwitterTweet

class StatusLocalRepositoryImplTest {

    lateinit var repository: StatusLocalRepositoryImpl

    val gson = Gson()
    val tweet = Tweet.from(gson.fromJson(readJsonFromAssets("tweet.json"), TwitterTweet::class.java))
    val tweet2 = Tweet.from(gson.fromJson(readJsonFromAssets("tweet_2.json"), TwitterTweet::class.java))

    @Before
    fun setup() {
        repository = StatusLocalRepositoryImpl()
    }

    @Test
    fun clear() {
        repository.previousTweetSubject.onNext(tweet.toOptional())

        repository.clear().test()
            .apply {
                assertNoValues()
                assertComplete()

                assertThat(repository.previousTweetSubject.hasValue()).isTrue()
                assertThat(repository.previousTweetSubject.value).isEqualTo(None)
            }
    }

    @Test
    fun getPrevious() {
        repository.previousTweetSubject.onNext(tweet.toOptional())

        repository.getPrevious().test()
            .apply {
                assertValue(tweet.toOptional())
                assertNotComplete()

                assertThat(repository.previousTweetSubject.value).isEqualTo(tweet.toOptional())
            }
    }

    @Test
    fun update() {
        repository.previousTweetSubject.onNext(tweet.toOptional())

        repository.update(tweet2).test()
            .apply {
                assertNoValues()
                assertComplete()

                assertThat(repository.previousTweetSubject.value).isEqualTo(tweet2.toOptional())
            }
    }
}
