package net.yslibrary.monotweety.base

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup
import com.bluelinelabs.conductor.*
import net.yslibrary.monotweety.base.di.Names
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import kotlin.properties.Delegates

abstract class BaseActivity : AppCompatActivity() {

    @set:Inject
    @setparam:Named(Names.FOR_ACTIVITY)
    var activityBus by Delegates.notNull<EventBus>()

    protected abstract val layoutResId: Int
    protected lateinit var router: Router
        private set

    protected abstract val container: ChangeHandlerFrameLayout
    protected abstract val rootController: Controller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate - BaseActivity")
        setContentView(layoutResId)

        router = Conductor.attachRouter(this, container, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(rootController))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.i("onActivityResult - BaseActivity")
        router.onActivityResult(requestCode, resultCode, data)
    }

    fun showSnackBar(message: String) = Snackbar.make(container.parent as ViewGroup, message, Snackbar.LENGTH_SHORT).show()
}
