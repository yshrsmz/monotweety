package net.yslibrary.monotweety.setting.adapterdelegate

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates2.AdapterDelegate
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.setting.SettingAdapter

/**
 * Created by yshrsmz on 2016/10/07.
 */
class SwitchAdapterDelegate : AdapterDelegate<List<SettingAdapter.Item>> {

  override fun isForViewType(items: List<SettingAdapter.Item>, position: Int): Boolean {
    return items[position] is SwitchItem
  }

  override fun onBindViewHolder(items: List<SettingAdapter.Item>, position: Int, holder: RecyclerView.ViewHolder, payloads: MutableList<Any?>?) {
    val item = items[position] as SwitchItem
    if (holder is SwitchViewHolder) {
      holder.settingSwitch.text = item.title
      holder.settingSwitch.isChecked = item.checked
      holder.settingSwitch.isEnabled = item.enabled
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
    return SwitchViewHolder.create(parent)
  }

  data class SwitchItem(val title: String, val checked: Boolean, val enabled: Boolean) : SettingAdapter.Item

  class SwitchViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val settingSwitch = view.findById<SwitchCompat>(R.id.setting_switch)

    companion object {
      fun create(parent: ViewGroup): SwitchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_switch, parent, false)
        return SwitchViewHolder(view)
      }
    }
  }
}