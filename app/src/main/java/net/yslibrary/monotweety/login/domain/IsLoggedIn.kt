package net.yslibrary.monotweety.login.domain

import io.reactivex.Single
import net.yslibrary.monotweety.base.di.AppScope
import net.yslibrary.monotweety.data.session.SessionRepository
import javax.inject.Inject

@AppScope
class IsLoggedIn @Inject constructor(private val sessionRepository: SessionRepository) {

  fun execute(): Single<Boolean> {
    return sessionRepository.getActiveSession()
        .map { token -> token.toNullable() != null }
  }
}