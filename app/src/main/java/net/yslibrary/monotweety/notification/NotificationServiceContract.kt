package net.yslibrary.monotweety.notification

/**
 * Created by yshrsmz on 2016/09/26.
 */
class NotificationServiceContract {
  interface View {
    fun showNotification()
    fun hideNotification()
    fun showTweetDialog(text: String)
    fun showTweetSucceeded()
    fun showTweetFailed()
    fun showTweetFailedBecauseOfLength()
  }


  interface Presenter {
    fun onShowNotificationCommand()
    fun onHideNotificationCommand()
    fun onDirectTweetCommand()
    fun onShowTweetDialogCommand()
  }
}