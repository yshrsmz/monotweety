package net.yslibrary.monotweety.appdata.status.remote

import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterSession

class TwitterApiClient(session: TwitterSession) : TwitterApiClient(session) {

    val updateStatusService: UpdateStatusService
        get() = getService(UpdateStatusService::class.java)
}
