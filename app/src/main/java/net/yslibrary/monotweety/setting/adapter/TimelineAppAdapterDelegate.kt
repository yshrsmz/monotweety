package net.yslibrary.monotweety.setting.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.data.appinfo.AppInfo

/**
 * Created by yshrsmz on 2016/11/14.
 */
class TimelineAppAdapterDelegate : AdapterDelegate<List<SettingAdapter.Item>>() {

  override fun isForViewType(items: List<SettingAdapter.Item>, position: Int): Boolean {
    return items[position] is Item
  }

  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
    return ViewHolder.create(parent)
  }

  override fun onBindViewHolder(items: List<SettingAdapter.Item>,
                                position: Int,
                                holder: RecyclerView.ViewHolder,
                                payloads: MutableList<Any>) {
    val item = items[position] as Item

    if (holder is ViewHolder) {
      val context = holder.itemView.context
      val res = context.resources

      val subTitle = if (item.checked)
        res.getString(R.string.sub_label_timelineapp_on, item.selectedApp!!.name)
      else
        res.getString(R.string.sub_label_timelineapp_off)

      holder.title.text = res.getString(R.string.label_timelineapp)
      holder.subTitle.text = subTitle
      holder.itemView.isEnabled = item.enabled
    }
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


  data class Item(val enabled: Boolean,
                  val checked: Boolean,
                  val apps: List<AppInfo>,
                  val selectedApp: AppInfo?) : SettingAdapter.Item {

    override val type: SettingAdapter.ViewType = SettingAdapter.ViewType.TIMELINE_APP
  }

  interface Listener {
    fun onTimelineAppChanged(enabled: Boolean, selectedApp: AppInfo?)
  }
}