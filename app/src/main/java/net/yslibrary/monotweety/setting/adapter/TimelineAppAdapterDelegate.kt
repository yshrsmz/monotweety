package net.yslibrary.monotweety.setting.adapter

import android.content.Context
import android.support.v7.app.AlertDialog
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
class TimelineAppAdapterDelegate(private val listener: Listener) : AdapterDelegate<List<SettingAdapter.Item>>() {

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

      val subTitle = if (item.selectedApp.installed)
        res.getString(R.string.sub_label_timelineapp_on, item.selectedApp.name)
      else
        res.getString(R.string.sub_label_timelineapp_off)

      holder.title.text = res.getString(R.string.label_timelineapp)
      holder.subTitle.text = subTitle
      holder.itemView.isEnabled = item.enabled
      holder.itemView.setOnClickListener { onClick(context, item) }
    }
  }

  private fun onClick(context: Context, item: Item) {
    val position = item.apps.indexOf(item.selectedApp)

    AlertDialog.Builder(context)
        .setTitle(R.string.label_timelineapp_description)
        .setSingleChoiceItems(
            (listOf(context.getString(R.string.label_timelineapp_initial_value)) + item.apps.map { it.name }).toTypedArray(),
            if (position < 0) 0 else position + 1,
            null)
        .setPositiveButton(R.string.label_confirm,
            { dialog, buttonPosition ->
              val selected = (dialog as AlertDialog).listView.checkedItemPosition
              val app = item.apps.getOrElse(selected - 1, { AppInfo.empty() })
              listener.onTimelineAppChanged(app)
            }).show()
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
                  val apps: List<AppInfo>,
                  val selectedApp: AppInfo) : SettingAdapter.Item {

    override val type: SettingAdapter.ViewType = SettingAdapter.ViewType.TIMELINE_APP
  }

  interface Listener {
    fun onTimelineAppChanged(selectedApp: AppInfo)
  }
}