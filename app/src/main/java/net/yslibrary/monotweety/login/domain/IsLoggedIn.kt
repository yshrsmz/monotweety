package net.yslibrary.monotweety.login.domain

import io.reactivex.Single
import net.yslibrary.monotweety.appdata.session.SessionRepository
import net.yslibrary.monotweety.base.di.AppScope
import javax.inject.Inject

@AppScope
class IsLoggedIn @Inject constructor(private val sessionRepository: SessionRepository) {

    fun execute(): Single<Boolean> {
        return sessionRepository.getActiveSession()
            .map { token -> token.toNullable() != null }
    }
}
