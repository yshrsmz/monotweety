package net.yslibrary.monotweety.ui.base

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

open class ViewBindingAppCompatActivity<T : ViewBinding>(
    @LayoutRes contentLayoutId: Int,
    bind: (View) -> T,
) : AppCompatActivity(contentLayoutId) {
    protected val binding by viewBinding(bind)
}

