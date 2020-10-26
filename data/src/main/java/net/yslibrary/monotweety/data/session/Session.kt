package net.yslibrary.monotweety.data.session

import com.codingfeline.twitter4kt.core.model.oauth1a.AccessToken

data class Session(
    val authToken: String,
    val authTokenSecret: String,
)

fun Session.toAccessToken(): AccessToken = AccessToken(authToken, authTokenSecret, "", "")
