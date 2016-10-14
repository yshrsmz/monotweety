package net.yslibrary.monotweety.status.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.inflate

/**
 * Created by yshrsmz on 2016/10/14.
 */
class PreviousStatusAdapterDelegate : AdapterDelegate<List<ComposeStatusAdapter.Item>>() {

  override fun onBindViewHolder(items: List<ComposeStatusAdapter.Item>,
                                position: Int,
                                holder: RecyclerView.ViewHolder,
                                payloads: MutableList<Any>) {

    if (holder is ViewHolder) {
      val item = items[position] as Item
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
    return ViewHolder.create(parent)
  }

  override fun isForViewType(items: List<ComposeStatusAdapter.Item>, position: Int): Boolean {
    return items[position].viewType == ComposeStatusAdapter.ViewType.PREVIOUS_STATUS
  }

  data class Item(val id: Long,
                  val status: String,
                  val createdAt: String,
                  override val viewType: ComposeStatusAdapter.ViewType = ComposeStatusAdapter.ViewType.PREVIOUS_STATUS) : ComposeStatusAdapter.Item

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
      fun create(parent: ViewGroup): ViewHolder {
        val view = parent.inflate(R.layout.vh_previous_status)
        return ViewHolder(view)
      }
    }
  }
}