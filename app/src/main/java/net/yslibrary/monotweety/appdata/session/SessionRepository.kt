package net.yslibrary.monotweety.appdata.session

import com.gojuno.koptional.Optional
import com.twitter.sdk.android.core.TwitterSession
import io.reactivex.Single

interface SessionRepository {
    fun getActiveSession(): Single<Optional<TwitterSession>>
}
