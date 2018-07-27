package net.yslibrary.monotweety.setting.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.findById

class SubHeaderAdapterDelegate : AdapterDelegate<List<SettingAdapter.Item>>() {

    override fun isForViewType(items: List<SettingAdapter.Item>, position: Int): Boolean {
        return items[position] is Item
    }

    override fun onBindViewHolder(items: List<SettingAdapter.Item>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any?>) {
        val item = items[position] as Item
        if (holder is ViewHolder) {
            holder.title.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder.create(parent)
    }

    data class Item(val title: String,
                    override val type: SettingAdapter.ViewType) : SettingAdapter.Item

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title = view.findById<TextView>(R.id.title)

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.vh_subheader, parent, false)
                return ViewHolder(view)
            }
        }
    }
}
