package net.yslibrary.monotweety.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.yslibrary.monotweety.App
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.data.session.toAccessToken
import net.yslibrary.monotweety.databinding.ActivityMainBinding
import net.yslibrary.monotweety.domain.session.ObserveSession
import net.yslibrary.monotweety.ui.base.ViewBindingAppCompatActivity
import net.yslibrary.monotweety.ui.di.HasComponent
import net.yslibrary.monotweety.ui.launcher.LauncherActivity

class MainActivity : ViewBindingAppCompatActivity<ActivityMainBinding>(
    R.layout.activity_main,
    ActivityMainBinding::bind,
), HasComponent<MainActivityComponent> {

    override lateinit var component: MainActivityComponent

    private val observeSession: ObserveSession by lazy {
        App.appComponent(this).observeSession()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = runBlocking { observeSession().first() }
        if (session == null) {
            Toast.makeText(this, "You need to login first", Toast.LENGTH_LONG).show()
            startActivity(LauncherActivity.getIntent(this))
            finish()
        } else {
            component = App.getOrCreateUserComponent(this, session.toAccessToken())
                .mainActivityComponent()
                .build(this)
        }

        component.inject(this)
    }

    companion object {
        fun callingIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }
}
