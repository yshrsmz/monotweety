package net.yslibrary.monotweety.notification

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import rx.Observable
import rx.subjects.BehaviorSubject
import timber.log.Timber


/**
 * Created by yshrsmz on 2016/09/26.
 */
class ServiceConnection(context: Context, private val intent: Intent) : android.content.ServiceConnection {

  private val context: Context = context.applicationContext
  private val subject = BehaviorSubject.create<NotificationService?>()

  override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
    Timber.d("Service connected")
    initWithBinder(binder)
  }

  override fun onServiceDisconnected(name: ComponentName?) {
    Timber.d("Service disconnected")
    dispose()
  }

  fun initWithBinder(binder: IBinder?) {
    if (binder == null) {
      Timber.d("Service not bound")
      subject.onNext(null)
      return
    }

    Timber.d("Service bound")
    val service = (binder as NotificationService.ServiceBinder).service
    subject.onNext(service)
  }

  fun dispose() {
    subject.onNext(null)
  }

  fun bindService(): Observable<NotificationService> {
    if (subject.value == null) {
      context.bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    return subject.asObservable()
        .filter { it != null }
        .map { it!! }
  }
}