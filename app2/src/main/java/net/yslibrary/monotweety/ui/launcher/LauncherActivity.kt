package net.yslibrary.monotweety.ui.launcher

import android.os.Bundle
import net.yslibrary.monotweety.App
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.analytics.Analytics
import net.yslibrary.monotweety.databinding.ActivityMainBinding
import net.yslibrary.monotweety.ui.base.ViewBindingAppCompatActivity
import net.yslibrary.monotweety.ui.di.HasComponent
import javax.inject.Inject

class LauncherActivity : ViewBindingAppCompatActivity<ActivityMainBinding>(
    R.layout.activity_launcher,
    ActivityMainBinding::bind
), HasComponent<LauncherActivityComponent> {

    @Inject
    lateinit var analyrics: Analytics

    override val component: LauncherActivityComponent by lazy {
        App.appComponent(this)
            .launcherActivityComponent()
            .build(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        component.inject(this)
    }
}
