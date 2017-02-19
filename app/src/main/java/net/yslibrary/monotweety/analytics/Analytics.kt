package net.yslibrary.monotweety.analytics

import android.os.Bundle
import android.support.annotation.StringDef
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
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, NAME_TWEET)
    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, EVENT_TWEET_FROM_NOTIFICATION)

    Timber.i("analytics event: %s", EVENT_TWEET_FROM_NOTIFICATION)
    analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
  }

  fun tweetFromNotificationButTooLong() {
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, NAME_TWEET)
    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, EVENT_TWEET_FROM_NOTIFICATION_BUT_TOO_LONG)

    Timber.i("analytics event: %s", EVENT_TWEET_FROM_NOTIFICATION_BUT_TOO_LONG)
    analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
  }

  fun loginCompleted() {
    val bundle = Bundle()
    analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)
  }

  fun tweetFromEditor() {
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, NAME_TWEET)
    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, EVENT_TWEET_FROM_EDITOR)

    Timber.i("analytics event: %s", EVENT_TWEET_FROM_EDITOR)
    analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
  }

  fun viewEvent(@ViewEventType name: String) {
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)

    Timber.i("analytics screen: %s", name)
    analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
  }

  @Retention(AnnotationRetention.SOURCE)
  @StringDef(VIEW_SPLASH, VIEW_LOGIN, VIEW_SETTING, VIEW_LICENSE, VIEW_CHANGELOG, VIEW_COMPOSE_STATUS)
  annotation class ViewEventType
}