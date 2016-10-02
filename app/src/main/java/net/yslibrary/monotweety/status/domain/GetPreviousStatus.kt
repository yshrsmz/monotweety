package net.yslibrary.monotweety.status.domain

import com.twitter.sdk.android.core.models.Tweet
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.status.StatusRepository
import rx.Observable
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/10/02.
 */
@UserScope
class GetPreviousStatus @Inject constructor(private val statusRepository: StatusRepository) {

  fun execute(): Observable<Tweet?> {
    return statusRepository.previousStatus()
  }
}