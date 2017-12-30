package net.yslibrary.monotweety.status.domain

import com.gojuno.koptional.Optional
import io.reactivex.Observable
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.status.StatusRepository
import net.yslibrary.monotweety.data.status.Tweet
import javax.inject.Inject

@UserScope
class GetPreviousStatus @Inject constructor(private val statusRepository: StatusRepository) {

  fun execute(): Observable<Optional<Tweet>> {
    return statusRepository.previousStatus()
  }
}