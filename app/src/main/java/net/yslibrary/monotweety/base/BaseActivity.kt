package net.yslibrary.monotweety.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bluelinelabs.conductor.*
import net.yslibrary.rxeventbus.EventBus
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/09/24.
 */
abstract class BaseActivity : AppCompatActivity() {

  @field:[Inject]
  lateinit var activityBus: EventBus

  protected abstract val layoutResId: Int
  protected lateinit var router: Router
    private set

  protected abstract val container: ChangeHandlerFrameLayout
  protected abstract val rootController: Controller

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
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
}