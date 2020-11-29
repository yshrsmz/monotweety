package net.yslibrary.monotweety.ui.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codingfeline.twitter4kt.core.model.oauth1a.AccessToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.yslibrary.monotweety.App
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.data.session.toAccessToken
import net.yslibrary.monotweety.ui.di.HasComponent

class ComposeActivity : AppCompatActivity(R.layout.activity_compose),
    HasComponent<ComposeActivityComponent> {

    private var accessToken: AccessToken? = null

    override val component by lazy {
        val token =
            requireNotNull(accessToken) { "AccessToken is required to create a UserComponent" }
        App.getOrCreateUserComponent(applicationContext, token)
            .composeActivityComponent()
            .build(this)
    }

    private val observeSession by lazy {
        App.appComponent(this).observeSession()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = runBlocking { observeSession().first() }
        if (session == null) {
            finish()
            return
        }
        accessToken = session.toAccessToken()

        component.inject(this)

        ComposeTweetDialogFragment.newInstance()
            .show(supportFragmentManager, ComposeTweetDialogFragment.TAG)
    }

    companion object {
        private const val KEY_STATUS = "key_status"

        fun callingIntent(context: Context, status: String? = null): Intent {
            return Intent(context.applicationContext, ComposeActivity::class.java)
                .apply {
                    if (!status.isNullOrBlank()) {
                        putExtra(KEY_STATUS, status)
                    }
                    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                            or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                            or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
        }
    }
}
