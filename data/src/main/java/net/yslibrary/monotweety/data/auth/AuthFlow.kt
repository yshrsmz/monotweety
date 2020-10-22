package net.yslibrary.monotweety.data.auth

import com.codingfeline.twitter4kt.core.getOrThrow
import com.codingfeline.twitter4kt.core.model.oauth1a.AccessToken
import com.codingfeline.twitter4kt.core.model.oauth1a.RequestToken
import com.codingfeline.twitter4kt.core.oauth1a.OAuth1aFlow
import com.codingfeline.twitter4kt.core.onSuccess
import net.yslibrary.monotweety.data.session.Session
import net.yslibrary.monotweety.data.session.SessionRepository
import javax.inject.Inject

interface AuthFlow {
    suspend fun getRequestToken(): RequestToken
    suspend fun getAccessToken(requestToken: RequestToken, verifierCode: String): AccessToken
}

internal class AuthFlowImpl @Inject constructor(
    private val oAuth1aFlow: OAuth1aFlow,
    private val sessionRepository: SessionRepository,
) : AuthFlow {

    override suspend fun getRequestToken(): RequestToken {
        return oAuth1aFlow.fetchRequestToken().getOrThrow()
    }

    override suspend fun getAccessToken(
        requestToken: RequestToken,
        verifierCode: String,
    ): AccessToken {
        return oAuth1aFlow.fetchAccessToken(requestToken.token, verifierCode)
            .onSuccess { sessionRepository.update(Session(it.token, it.secret)) }
            .getOrThrow()
    }
}
