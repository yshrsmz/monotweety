package net.yslibrary.monotweety.status.domain

import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.status.StatusRepository
import net.yslibrary.monotweety.data.status.Tweet
import rx.Observable
import javax.inject.Inject

@UserScope
class GetPreviousStatus @Inject constructor(private val statusRepository: StatusRepository) {

  fun execute(): Observable<Tweet?> {
    return statusRepository.previousStatus()
  }
}