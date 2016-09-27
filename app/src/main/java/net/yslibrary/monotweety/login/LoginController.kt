package net.yslibrary.monotweety.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.BaseController
import net.yslibrary.monotweety.base.HasComponent
import net.yslibrary.monotweety.base.findById

/**
 * Created by yshrsmz on 2016/09/27.
 */
class LoginController : BaseController(), HasComponent<LoginComponent> {

  lateinit var bindings: Bindings

  override val component: LoginComponent by lazy {
    getComponentProvider<LoginComponent.ComponentProvider>(activity)
        .loginComponent(LoginViewModule())
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = inflater.inflate(R.layout.controller_login, container, false)
    bindings = Bindings(view)

    component.inject(this)

    setEvents()

    return view
  }

  fun setEvents() {
    bindings.loginButton.callback = object : Callback<TwitterSession>() {
      override fun success(result: Result<TwitterSession>?) {

      }

      override fun failure(exception: TwitterException?) {

      }
    }
  }

  inner class Bindings(view: View) {
    val loginButton = view.findById<TwitterLoginButton>(R.id.login)
  }
}