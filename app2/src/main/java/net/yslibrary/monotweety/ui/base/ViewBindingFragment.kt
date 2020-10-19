package net.yslibrary.monotweety.ui.base

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

open class ViewBindingFragment<T : ViewBinding>(
    @LayoutRes contentLayoutId: Int,
    bind: (View) -> T
) : Fragment(contentLayoutId) {
    protected val binding: T by viewBinding(bind)
}
