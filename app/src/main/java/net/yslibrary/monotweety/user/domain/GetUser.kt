package net.yslibrary.monotweety.user.domain

import com.gojuno.koptional.Optional
import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterSession
import io.reactivex.Flowable
import net.yslibrary.monotweety.appdata.user.User
import net.yslibrary.monotweety.appdata.user.UserRepository
import net.yslibrary.monotweety.base.di.UserScope
import timber.log.Timber
import javax.inject.Inject

@UserScope
class GetUser @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager<TwitterSession>
) {

    private var progress: Progress = Progress.IDLE

    fun execute(): Flowable<Optional<User>> {
        return Flowable.defer {
            val session = sessionManager.activeSession
            userRepository.get(session.id)
        }.doOnNext {
            if (progress == Progress.LOADING || userRepository.isValid(it.toNullable())) {
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
