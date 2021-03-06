package net.yslibrary.monotweety.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T : ViewBinding> FragmentActivity.viewBinding(bind: (View) -> T): Lazy<T> = object : Lazy<T> {
    private var binding: T? = null
    override fun isInitialized(): Boolean = binding != null
    override val value: T
        get() = binding ?: bind(getContentView()).also {
            binding = it
        }

    private fun FragmentActivity.getContentView(): View {
        return checkNotNull(findViewById<ViewGroup>(android.R.id.content).getChildAt(0)) {
            "Call setContentView or Use Activity's secondary constructor passing layout res id."
        }
    }
}

fun <T : ViewBinding> Fragment.viewBinding(bind: (View) -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T> {
        @Suppress("UNCHECKED_CAST")
        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
            (requireView().getTag(property.name.hashCode()) as? T)?.let { return it }
            return bind(requireView()).also {
                requireView().setTag(property.name.hashCode(), it)
            }
        }
    }

open class ViewBindingAppCompatActivity<T : ViewBinding>(
    @LayoutRes contentLayoutId: Int,
    bind: (View) -> T,
) : AppCompatActivity(contentLayoutId) {
    protected val binding by viewBinding(bind)
}

open class ViewBindingFragment<T : ViewBinding>(
    @LayoutRes contentLayoutId: Int,
    bind: (View) -> T,
) : Fragment(contentLayoutId) {
    protected val binding: T by viewBinding(bind)
}

open class ViewBindingBottomSheetDialogFragment<T : ViewBinding>(
    @LayoutRes private val contentLayoutId: Int,
    bind: (View) -> T,
) : BottomSheetDialogFragment() {

    protected val binding: T by viewBinding(bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(contentLayoutId, container, false)
    }
}
