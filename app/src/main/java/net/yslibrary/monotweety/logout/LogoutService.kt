package net.yslibrary.monotweety.logout

import android.app.IntentService
import android.content.Context
import android.content.Intent
import net.yslibrary.monotweety.App
import net.yslibrary.monotweety.activity.main.MainActivity
import net.yslibrary.monotweety.login.domain.DoLogout
import javax.inject.Inject
import kotlin.properties.Delegates

class LogoutService : IntentService(NAME) {

    @set:[Inject]
    var doLogout by Delegates.notNull<DoLogout>()

    val component: LogoutComponent by lazy {
        DaggerLogoutComponent.builder()
            .userComponent(App.userComponent(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        component.inject(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        doLogout.execute().blockingAwait()
        App.clearUserComponent(this)

        val activityIntent = MainActivity.callingIntent(this)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        startActivity(activityIntent)
    }

    companion object {
        const val NAME = "LogoutService"

        fun callingIntent(context: Context): Intent {
            return Intent(context, LogoutService::class.java)
        }
    }
}
