package net.yslibrary.monotweety.user.domain

import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterSession
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.user.User
import net.yslibrary.monotweety.data.user.UserRepository
import rx.Observable
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/10/08.
 */
@UserScope
class GetUser @Inject constructor(private val userRepository: UserRepository,
                                  private val sessionManager: SessionManager<TwitterSession>) {

  private var progress: Progress = Progress.IDLE

  fun execute(): Observable<User?> {
    return Observable.defer {
      val session = sessionManager.activeSession
      userRepository.get(session.id)
    }.doOnNext {
      if (progress == Progress.LOADING || userRepository.isValid(it)) {
        // no-op
        Timber.d("currently loading user or cache is valid")
      } else {
        Timber.d("start fetching user")
        progress = Progress.LOADING
        userRepository.fetch()
            .subscribe({
              Timber.d("loading user finished")
              progress = Progress.LOADED
            }, {
              progress = Progress.IDLE
              Timber.e(it, it.message)
            })
      }
    }
  }

  enum class Progress {
    IDLE,
    LOADING,
    LOADED
  }
}