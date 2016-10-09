package net.yslibrary.monotweety.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.ActionBarController
import net.yslibrary.monotweety.base.HasComponent
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.event.ActivityResult
import net.yslibrary.monotweety.setting.SettingController
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/09/27.
 */
class LoginController : ActionBarController(), HasComponent<LoginComponent> {

  override val shouldShowActionBar: Boolean = false

  lateinit var bindings: Bindings

  @field:[Inject]
  lateinit var viewModel: LoginViewModel

  override val component: LoginComponent by lazy {
    getComponentProvider<LoginComponent.ComponentProvider>(activity)
        .loginComponent(LoginViewModule())
  }

  override fun onCreate() {
    super.onCreate()
    component.inject(this)
  }

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = inflater.inflate(R.layout.controller_login, container, false)

    bindings = Bindings(view)

    setEvents()

    return view
  }

  fun setEvents() {
    activityBus.on(ActivityResult::class.java)
        .observeOn(AndroidSchedulers.mainThread())
        .bindToLifecycle()
        .subscribe { bindings.loginButton.onActivityResult(it.requestCode, it.resultCode, it.data) }

    viewModel.loginCompleted
        .observeOn(AndroidSchedulers.mainThread())
        .bindToLifecycle()
        .doOnNext { toast(getString(R.string.message_login_succeeded, it.userName)).show() }
        .subscribe {
          router.setRoot(RouterTransaction.with(SettingController())
              .popChangeHandler(FadeChangeHandler())
              .pushChangeHandler(FadeChangeHandler()))
        }

    viewModel.loginFailed
        .observeOn(AndroidSchedulers.mainThread())
        .bindToLifecycle()
        .subscribe { showSnackBar(getString(R.string.error_login_failed)) }

    bindings.loginButton.callback = object : Callback<TwitterSession>() {
      override fun success(result: Result<TwitterSession>) {
        Timber.d("login success: $result")
        viewModel.onLoginCompleted(result.data)
      }

      override fun failure(exception: TwitterException) {
        Timber.e(exception, exception.message)
        viewModel.onLoginFailed(exception)
      }
    }
  }

  inner class Bindings(view: View) {
    val loginButton = view.findById<TwitterLoginButton>(R.id.login)
  }
}