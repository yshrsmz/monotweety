package net.yslibrary.monotweety.ui.settings.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import net.yslibrary.monotweety.ui.base.groupie.GroupieViewHolder

private val ATTRS = intArrayOf(android.R.attr.listDivider)

class DividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val divider: Drawable

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        divider = a.getDrawable(0)!!
        a.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        (0 until parent.childCount).forEach { index ->
            val child = parent.getChildAt(index)
            val holder = parent.getChildViewHolder(child)
            if (holder is GroupieViewHolder<*> && holder.item is SubHeaderItem) {
                val params = child.layoutParams as RecyclerView.LayoutParams

                val bottom = child.top - params.topMargin
                val top = bottom - divider.intrinsicHeight

                divider.setBounds(left, top, right, bottom)
                divider.draw(c)
            }
        }
    }
}
