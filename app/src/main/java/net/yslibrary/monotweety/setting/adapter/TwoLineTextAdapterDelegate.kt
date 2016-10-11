package net.yslibrary.monotweety.setting.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.findById

/**
 * Created by yshrsmz on 2016/10/08.
 */
class TwoLineTextAdapterDelegate(private val listener: Listener) : AdapterDelegate<List<SettingAdapter.Item>>() {

  override fun isForViewType(items: List<SettingAdapter.Item>, position: Int): Boolean {
    return items[position] is Item
  }

  override fun onBindViewHolder(items: List<SettingAdapter.Item>, position: Int, holder: RecyclerView.ViewHolder, payloads: MutableList<Any?>) {
    val item = items[position] as Item
    if (holder is ViewHolder) {
      holder.title.text = item.title
      holder.subTitle.text = item.subTitle
      holder.itemView.isEnabled = item.enabled
      holder.itemView.setOnClickListener { listener.onClick(item) }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
    return ViewHolder.create(parent)
  }

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val title = view.findById<TextView>(R.id.title)
    val subTitle = view.findById<TextView>(R.id.sub_title)

    companion object {
      fun create(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vh_2line_text, parent, false)
        return ViewHolder(view)
      }
    }

  }

  data class Item(val title: String,
                  val subTitle: String,
                  val enabled: Boolean, override val type: SettingAdapter.ViewType) : SettingAdapter.Item

  interface Listener {
    fun onClick(item: Item)
  }
}