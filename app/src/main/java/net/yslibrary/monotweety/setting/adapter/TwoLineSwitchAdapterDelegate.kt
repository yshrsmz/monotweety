package net.yslibrary.monotweety.setting.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.base.inflate

class TwoLineSwitchAdapterDelegate(
    private val listener: Listener
) : AdapterDelegate<List<SettingAdapter.Item>>() {
    override fun isForViewType(items: List<SettingAdapter.Item>, position: Int): Boolean {
        return items[position] is Item
    }

    override fun onBindViewHolder(
        items: List<SettingAdapter.Item>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val item = items[position] as Item
        if (holder is ViewHolder) {
            holder.title.text = item.title
            holder.subTitle.text = item.subTitle
            holder.switch.setOnCheckedChangeListener(null)
            holder.switch.isChecked = item.checked
            holder.itemView.isEnabled = item.enabled
            holder.switch.setOnCheckedChangeListener { _, isChecked ->
                listener.onClick(item, isChecked)
            }
            holder.itemView.setOnClickListener { holder.switch.performClick() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder.create(parent)
    }

    data class Item(
        val title: String,
        val subTitle: String,
        val checked: Boolean,
        val enabled: Boolean,
        override val type: SettingAdapter.ViewType
    ) : SettingAdapter.Item

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView = view.findById(R.id.title)
        val subTitle: TextView = view.findById(R.id.sub_title)
        val switch: SwitchCompat = view.findById(R.id.switch_button)

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view = parent.inflate(R.layout.vh_2line_switch)
                return ViewHolder(view)
            }
        }
    }

    interface Listener {
        fun onClick(item: Item, checked: Boolean)
    }
}
