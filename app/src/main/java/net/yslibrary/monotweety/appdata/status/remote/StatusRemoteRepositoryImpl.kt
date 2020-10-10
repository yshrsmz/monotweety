package net.yslibrary.monotweety.appdata.status.remote

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import io.reactivex.Single
import net.yslibrary.monotweety.appdata.status.Tweet
import net.yslibrary.monotweety.di.UserScope
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import javax.inject.Inject
import com.twitter.sdk.android.core.models.Tweet as TwitterTweet

@UserScope
class StatusRemoteRepositoryImpl @Inject constructor(private val statusesService: UpdateStatusService) :
    StatusRemoteRepository {

    override fun update(status: String, inReplyToStatusId: Long?): Single<Tweet> {
        return Single.create<TwitterTweet> { emitter ->
            val call = statusesService.update(
                encodeStatus(status),
                inReplyToStatusId,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
            call.enqueue(object : Callback<TwitterTweet>() {
                override fun success(result: Result<TwitterTweet>) {
                    emitter.onSuccess(result.data)
                }

                override fun failure(exception: TwitterException) {
                    emitter.onError(exception)
                }
            })
            emitter.setCancellable { call.cancel() }
        }
            .map {
                Tweet(
                    id = it.id,
                    inReplyToStatusId = it.inReplyToStatusId,
                    text = it.text,
                    createdAt = it.createdAt
                )
            }
    }

    /**
     * encode status and replace `*` with `%2A`
     * https://github.com/twitter/twitter-kit-android/issues/68
     */
    fun encodeStatus(status: String): String {
        var encoded = status

        try {
            encoded = URLEncoder.encode(status, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            // no-op
        }
        encoded = encoded.replace("*", "%2A")

        return encoded
    }
}
