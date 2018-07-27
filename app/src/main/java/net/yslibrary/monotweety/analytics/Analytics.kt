package net.yslibrary.monotweety.analytics

import android.os.Bundle
import androidx.annotation.StringDef
import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber
import javax.inject.Inject

class Analytics @Inject constructor(private val analytics: FirebaseAnalytics) {

    companion object {
        private const val NAME_TWEET = "tweet"

        private const val EVENT_TWEET_FROM_EDITOR = "tweet_from_editor"
        private const val EVENT_TWEET_FROM_NOTIFICATION = "tweet_from_notification"
        private const val EVENT_TWEET_FROM_NOTIFICATION_BUT_TOO_LONG = "tweet_from_notification_but_too_long"

        const val VIEW_SPLASH = "splash"
        const val VIEW_LOGIN = "login"
        const val VIEW_SETTING = "setting"
        const val VIEW_COMPOSE_STATUS = "compose_tweet"
        const val VIEW_LICENSE = "license"
        const val VIEW_CHANGELOG = "changelog"
    }

    fun tweetFromNotification() {
        Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_NAME, NAME_TWEET)
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, EVENT_TWEET_FROM_NOTIFICATION)
        }.let {
            Timber.i("analytics event: %s", EVENT_TWEET_FROM_NOTIFICATION)
            analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, it)
        }
    }

    fun tweetFromNotificationButTooLong() {
        Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_NAME, NAME_TWEET)
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, EVENT_TWEET_FROM_NOTIFICATION_BUT_TOO_LONG)
        }.let {
            Timber.i("analytics event: %s", EVENT_TWEET_FROM_NOTIFICATION_BUT_TOO_LONG)
            analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, it)
        }
    }

    fun loginCompleted() {
        Bundle().let { analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, it) }
    }

    fun tweetFromEditor() {
        Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_NAME, NAME_TWEET)
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, EVENT_TWEET_FROM_EDITOR)
        }.let {
            Timber.i("analytics event: %s", EVENT_TWEET_FROM_EDITOR)
            analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, it)
        }
    }

    fun viewEvent(@ViewEventType name: String) {
        Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        }.let {
            Timber.i("analytics screen: %s", name)
            analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, it)
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(VIEW_SPLASH, VIEW_LOGIN, VIEW_SETTING, VIEW_LICENSE, VIEW_CHANGELOG, VIEW_COMPOSE_STATUS)
    annotation class ViewEventType
}
