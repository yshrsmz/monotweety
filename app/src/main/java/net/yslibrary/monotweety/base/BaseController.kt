package net.yslibrary.monotweety.base

import android.os.Build
import android.widget.Toast
import androidx.annotation.StringRes
import com.bluelinelabs.conductor.rxlifecycle2.RxController
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import me.drakeet.support.toast.ToastCompat
import net.yslibrary.monotweety.analytics.Analytics
import net.yslibrary.monotweety.base.di.Names
import javax.inject.Inject
import javax.inject.Named
import kotlin.properties.Delegates

abstract class BaseController : RxController() {

    @set:Inject
    @setparam:Named(Names.FOR_ACTIVITY)
    var activityBus by Delegates.notNull<EventBus>()

    @set:Inject
    var analytics by Delegates.notNull<Analytics>()

    @Suppress("UNCHECKED_CAST")
    fun <PROVIDER> getComponentProvider(parent: Any): PROVIDER {
        return ((parent as HasComponent<Any>).component as PROVIDER)
    }

    fun <T> Observable<T>.bindToLifecycle(): Observable<T> =
        this.compose(this@BaseController.bindToLifecycle<T>())

    fun <T> Single<T>.bindToLifecycle(): Single<T> =
        this.compose(this@BaseController.bindToLifecycle<T>())

    fun <T> Completable.bindToLifecycle(): Completable =
        this.compose(this@BaseController.bindToLifecycle<T>())

    fun showSnackBar(message: String) = (activity as BaseActivity).showSnackBar(message)

    fun toast(message: String): Toast? = applicationContext?.let {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            ToastCompat.makeText(it, message, Toast.LENGTH_SHORT)
        } else {
            Toast.makeText(it, message, Toast.LENGTH_SHORT)
        }

    }

    fun toastLong(message: String): Toast? = applicationContext?.let {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            ToastCompat.makeText(it, message, Toast.LENGTH_SHORT)
        } else {
            Toast.makeText(it, message, Toast.LENGTH_LONG)
        }
    }

    fun getString(@StringRes id: Int): String = applicationContext?.getString(id) ?: ""

    fun getString(@StringRes id: Int, vararg formatArgs: Any): String =
        applicationContext?.getString(id, *formatArgs) ?: ""
}
