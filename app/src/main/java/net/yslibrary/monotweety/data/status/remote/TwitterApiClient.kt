package net.yslibrary.monotweety.data.status.remote

import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterSession

/**
 * Created by yshrsmz on 2016/10/24.
 */
class TwitterApiClient(session: TwitterSession) : TwitterApiClient(session) {

  val updateStatusService: UpdateStatusService
    get() = getService(UpdateStatusService::class.java)
}