package net.yslibrary.monotweety.analytics

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import kotlin.reflect.KClass

class Analytics @Inject constructor(
    private val analytics: FirebaseAnalytics,
) {
    fun screenView(screen: Screen, clazz: KClass<*>) {
        analytics.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            bundleOf(
                FirebaseAnalytics.Param.SCREEN_NAME to screen.name,
                FirebaseAnalytics.Param.SCREEN_CLASS to clazz.simpleName,
            )
        )
    }

    fun logEvent() {
        
    }

    sealed class Screen(val name: String) {
        object Splash : Screen("splash")
        object Login : Screen("login")
        object Settings : Screen("setting")
        object License : Screen("compose_tweet")
        object Changelog : Screen("license")
        object Compose : Screen("changelog")
    }
}
