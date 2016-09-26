package net.yslibrary.monotweety.notification

import rx.Observable
import rx.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit


/**
 * Created by yshrsmz on 2016/09/26.
 */
class NotificationServiceViewModel {

  private val tweetableTextSubject = PublishSubject.create<String?>()

  private val overlongTextSubject = PublishSubject.create<String?>()

  private val updateCompletedSubject = PublishSubject.create<Unit>()

  val tweetableText: Observable<String?>
    get() = tweetableTextSubject.asObservable()

  val overlongText: Observable<String?>
    get() = overlongTextSubject.asObservable()

  val updateCompleted: Observable<Unit>
    get() = updateCompletedSubject.asObservable()

  fun onShowNotificationCommand() {
    Timber.d("onShowNotificationCommand")
  }

  fun onHideNotificationCommand() {
    Timber.d("onHideNotificationCommand")
  }

  fun onDirectTweetCommand(text: String) {
    Timber.d("onDirectTweetCommand: $text")
    Observable.interval(3, TimeUnit.SECONDS)
        .first()
        .subscribe {
          Timber.d("tweet completed")
          updateCompletedSubject.onNext(Unit)
        }
  }

  fun onShowTweetDialogCommand() {
    Timber.d("onShowTweetDialogCommnad")
  }
}