package net.yslibrary.monotweety.notification

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.gojuno.koptional.rxjava2.filterSome
import com.gojuno.koptional.toOptional
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber


class ServiceConnection(
    context: Context, private val intent: Intent
) : android.content.ServiceConnection {

    private val context: Context = context.applicationContext
    private val subject = BehaviorSubject.create<Optional<NotificationService>>()

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
            subject.onNext(None)
            return
        }

        Timber.d("Service bound")
        val service = (binder as NotificationService.ServiceBinder).service
        subject.onNext(service.toOptional())
    }

    fun dispose() {
        subject.onNext(None)
    }

    fun bindService(): Observable<NotificationService> {
        if (subject.value == null) {
            context.bindService(intent, this, Context.BIND_AUTO_CREATE)
        }

        return subject
            .filterSome()
    }
}
