package net.yslibrary.monotweety.setting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.findById

class OneLineTextAdapterDelegate(
    private val listener: Listener
) : AdapterDelegate<List<SettingAdapter.Item>>() {

    override fun isForViewType(items: List<SettingAdapter.Item>, position: Int): Boolean {
        return items[position] is Item
    }

    override fun onBindViewHolder(
        items: List<SettingAdapter.Item>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any?>
    ) {
        val item = items[position] as Item
        if (holder is OneLineTextAdapterDelegate.ViewHolder) {
            holder.title.text = item.title
            holder.itemView.isEnabled = item.enabled
            holder.itemView.setOnClickListener { listener.onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder.create(parent)
    }

    data class Item(
        val title: String,
        val enabled: Boolean,
        override val type: SettingAdapter.ViewType
    ) : SettingAdapter.Item

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title = view.findById<TextView>(R.id.title)

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.vh_1line_text, parent, false)
                return ViewHolder(view)
            }
        }
    }

    interface Listener {
        fun onClick(item: Item)
    }
}
