package net.yslibrary.monotweety.splash

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.SimpleSwapChangeHandler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.analytics.Analytics
import net.yslibrary.monotweety.base.ActionBarController
import net.yslibrary.monotweety.base.HasComponent
import net.yslibrary.monotweety.base.RefWatcherDelegate
import net.yslibrary.monotweety.login.LoginController
import net.yslibrary.monotweety.login.LoginTransitionChangeHandlerCompat
import net.yslibrary.monotweety.setting.SettingController
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.properties.Delegates

class SplashController : ActionBarController(), HasComponent<SplashComponent> {

    @set:[Inject]
    var viewModel by Delegates.notNull<SplashViewModel>()

    @set:[Inject]
    var refWatcherDelegate by Delegates.notNull<RefWatcherDelegate>()

    var disposables: CompositeDisposable = CompositeDisposable()

    override val shouldShowActionBar: Boolean = false

    override val component: SplashComponent by lazy {
        Timber.i("create SplashComponent")
        getComponentProvider<SplashComponent.ComponentProvider>(activity!!)
            .splashComponent(SplashViewModule())
    }

    override fun onContextAvailable(context: Context) {
        super.onContextAvailable(context)
        Timber.i("onContextAvailable - SplashController")
        component.inject(this)
        analytics.viewEvent(Analytics.VIEW_SPLASH)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_splash, container, false)

        disposables = CompositeDisposable()
        setEvents()

        return view
    }

    fun setEvents() {
        viewModel.loggedIn
            .zipWith(Single.timer(500, TimeUnit.MILLISECONDS).toObservable(), BiFunction { t1: Boolean, _: Long -> t1 })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val next: Controller = if (it) SettingController() else LoginController()

                router.setRoot(RouterTransaction.with(next)
                    .pushChangeHandler(if (it) SimpleSwapChangeHandler() else LoginTransitionChangeHandlerCompat())
                    .popChangeHandler(if (it) SimpleSwapChangeHandler() else LoginTransitionChangeHandlerCompat()))
            }.addTo(disposables)
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        disposables.dispose()
    }

    override fun onChangeEnded(changeHandler: ControllerChangeHandler, changeType: ControllerChangeType) {
        super.onChangeEnded(changeHandler, changeType)
        refWatcherDelegate.handleOnChangeEnded(isDestroyed, changeType)
    }

    override fun onDestroy() {
        super.onDestroy()
        refWatcherDelegate.handleOnDestroy()
    }
}
