package net.yslibrary.monotweety.changelog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import net.yslibrary.monotweety.App
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.analytics.Analytics
import net.yslibrary.monotweety.base.ActionBarController
import net.yslibrary.monotweety.base.ObjectWatcherDelegate
import javax.inject.Inject
import kotlin.properties.Delegates

class ChangelogController : ActionBarController() {

    @set:[Inject]
    var objectWatcherDelegate by Delegates.notNull<ObjectWatcherDelegate>()

    override val hasBackButton: Boolean = true

    override val title: String?
        get() = applicationContext?.getString(R.string.title_changelog)

    val component: ChangelogComponent by lazy {
        val activityBus =
            getComponentProvider<ChangelogViewModule.DependencyProvider>(activity!!).activityBus()
        DaggerChangelogComponent.builder()
            .userComponent(App.userComponent(applicationContext!!))
            .changelogViewModule(ChangelogViewModule(activityBus))
            .build()
    }

    override fun onContextAvailable(context: Context) {
        super.onContextAvailable(context)
        component.inject(this)
        analytics.viewEvent(Analytics.VIEW_CHANGELOG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_changelog, container, false)
    }

    override fun onChangeEnded(
        changeHandler: ControllerChangeHandler,
        changeType: ControllerChangeType
    ) {
        super.onChangeEnded(changeHandler, changeType)
        objectWatcherDelegate.handleOnChangeEnded(isDestroyed, changeType)
    }

    override fun onDestroy() {
        super.onDestroy()
        objectWatcherDelegate.handleOnDestroy()
    }
}
