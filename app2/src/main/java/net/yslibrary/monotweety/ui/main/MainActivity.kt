package net.yslibrary.monotweety.ui.main

import android.os.Bundle
import net.yslibrary.monotweety.App
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.databinding.ActivityMainBinding
import net.yslibrary.monotweety.ui.base.ViewBindingAppCompatActivity
import net.yslibrary.monotweety.ui.di.HasComponent

class MainActivity : ViewBindingAppCompatActivity<ActivityMainBinding>(
    R.layout.activity_main,
    ActivityMainBinding::bind,
), HasComponent<MainActivityComponent> {

    override val component: MainActivityComponent by lazy {
        App.userComponent(this)
            .mainActivityComponent()
            .build(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }
}
