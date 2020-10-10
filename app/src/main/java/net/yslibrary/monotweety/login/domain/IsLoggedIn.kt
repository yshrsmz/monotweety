package net.yslibrary.monotweety.login.domain

import io.reactivex.Single
import net.yslibrary.monotweety.appdata.session.SessionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsLoggedIn @Inject constructor(private val sessionRepository: SessionRepository) {

    fun execute(): Single<Boolean> {
        return sessionRepository.getActiveSession()
            .map { token -> token.toNullable() != null }
    }
}
