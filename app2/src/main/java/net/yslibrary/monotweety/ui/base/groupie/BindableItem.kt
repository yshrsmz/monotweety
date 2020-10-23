package net.yslibrary.monotweety.ui.base.groupie

import android.view.View
import androidx.viewbinding.ViewBinding
import com.xwray.groupie.Item

/**
 * The base unit of content for a GroupAdapter.
 * <p>
 * Because an Item is a Group of size one, you don't need to use Groups directly if you don't want;
 * simply mix and match Items and add directly to the adapter.
 * <p>
 * If you want to use Groups, because Item extends Group, you can mix and match adding Items and
 * other Groups directly to the adapter.
 *
 * @param <T> The ViewBinding subclass associated with this Item.
 * @param binder
 */
abstract class BindableItem<T : ViewBinding>(
    val binder: (view: View) -> T,
) : Item<GroupieViewHolder<T>>() {

    final override fun createViewHolder(itemView: View): GroupieViewHolder<T> {
        val binding = binder(itemView)
        return GroupieViewHolder(binding)
    }

    final override fun bind(viewHolder: GroupieViewHolder<T>, position: Int) {
        throw RuntimeException("Doesn't get called")
    }

    final override fun bind(
        viewHolder: GroupieViewHolder<T>,
        position: Int,
        payloads: List<Any>,
    ) {
        bind(viewHolder.binding, position, payloads)
    }

    /**
     * Perform any actions required to set up the view for display.
     *
     * @param viewBinding The ViewBinding to bind
     * @param position The adapter position
     */
    abstract fun bind(viewBinding: T, position: Int)

    /**
     * Perform any actions required to set up the view for display.
     *
     * If you don't specify how to handle payloads in your implementation, they'll be ignored and
     * the adapter will do a full rebind.
     *
     * @param viewBinding The ViewBinding to bind
     * @param position The adapter position
     * @param payloads A list of payloads (may be empty)
     */
    open fun bind(viewBinding: T, position: Int, payloads: List<Any>) {
        bind(viewBinding, position)
    }
}
