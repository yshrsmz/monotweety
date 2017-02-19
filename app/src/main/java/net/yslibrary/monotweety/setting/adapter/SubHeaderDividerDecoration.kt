package net.yslibrary.monotweety.setting.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView

class SubHeaderDividerDecoration(context: Context) : RecyclerView.ItemDecoration() {

  private val divider: Drawable

  init {
    val a = context.obtainStyledAttributes(ATTRS)
    divider = a.getDrawable(0)
    a.recycle()
  }

  override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    val left = parent.paddingLeft
    val right = parent.width - parent.paddingRight

    0.rangeTo(parent.childCount - 1)
        .forEach {
          val child = parent.getChildAt(it)
          val holder = parent.getChildViewHolder(child)
          if (holder is SubHeaderAdapterDelegate.ViewHolder && it != 0) {
            val params = child.layoutParams as RecyclerView.LayoutParams

            val bottom = child.top - params.topMargin
            val top = bottom - divider.intrinsicHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
          }
        }
  }

  companion object {
    private val ATTRS = intArrayOf(android.R.attr.listDivider)
  }
}