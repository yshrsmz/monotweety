package net.yslibrary.monotweety.login.domain

import net.yslibrary.monotweety.base.di.AppScope
import net.yslibrary.monotweety.data.session.SessionRepository
import rx.Single
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/09/27.
 */
@AppScope
class IsLoggedIn @Inject constructor(private val sessionRepository: SessionRepository) {

  fun execute(): Single<Boolean> {
    return sessionRepository.getActiveSession()
        .map { token -> token != null }
  }
}