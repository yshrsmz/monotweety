package net.yslibrary.monotweety.setting.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.findById

class SwitchAdapterDelegate(private val listener: Listener) : AdapterDelegate<List<SettingAdapter.Item>>() {

    override fun isForViewType(items: List<SettingAdapter.Item>, position: Int): Boolean {
        return items[position] is Item
    }

    override fun onBindViewHolder(items: List<SettingAdapter.Item>, position: Int, holder: RecyclerView.ViewHolder, payloads: MutableList<Any?>) {
        val item = items[position] as Item
        if (holder is SwitchViewHolder) {
            holder.switchButton.text = item.title
            holder.switchButton.isChecked = item.checked
            holder.switchButton.isEnabled = item.enabled

            holder.switchButton.setOnCheckedChangeListener { _, checked -> listener.onClick(item, checked) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return SwitchViewHolder.create(parent)
    }

    data class Item(val title: String,
                    val checked: Boolean,
                    val enabled: Boolean,
                    override val type: SettingAdapter.ViewType) : SettingAdapter.Item

    class SwitchViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val switchButton = view.findById<SwitchCompat>(R.id.switch_button)

        companion object {
            fun create(parent: ViewGroup): SwitchViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.vh_switch, parent, false)
                return SwitchViewHolder(view)
            }
        }
    }

    interface Listener {
        fun onClick(item: Item, checked: Boolean)
    }
}
