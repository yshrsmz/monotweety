package net.yslibrary.monotweety.status.adapter

import android.support.design.widget.TextInputEditText
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.jakewharton.rxbinding.widget.afterTextChangeEvents
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.base.inflate

/**
 * Created by yshrsmz on 2016/10/13.
 */
class EditorAdapterDelegate : AdapterDelegate<List<ComposeStatusAdapter.Item>>() {

  override fun isForViewType(items: List<ComposeStatusAdapter.Item>, position: Int): Boolean {
    return items[position].viewType == ComposeStatusAdapter.ViewType.EDITOR
  }

  override fun onBindViewHolder(items: List<ComposeStatusAdapter.Item>,
                                position: Int,
                                holder: RecyclerView.ViewHolder,
                                payloads: MutableList<Any>) {

    if (holder is ViewHolder) {
      val item = items[position] as Item

      val shouldUpdateStatus = item.clear || item.initialValue

      if (shouldUpdateStatus) {
        holder.statusInput.setText(item.status, TextView.BufferType.EDITABLE)
      }
      holder.statusCounter.text = item.statusLength.toString()

      // update only if value is updated at somewhere
      if (item.keepDialogOpen != holder.keepDialogOpenSwitch.isChecked) {
        holder.keepDialogOpenSwitch.isChecked = item.keepDialogOpen
      }

      if (item.enableThread != holder.enableThreadSwitch.isChecked) {
        holder.enableThreadSwitch.isChecked = item.enableThread
      }

      holder.statusInput.afterTextChangeEvents()
        .subscribe {  }

    }
  }

  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
    return ViewHolder.create(parent)
  }

  data class Item(val status: String,
                  val statusLength: Int,
                  val keepDialogOpen: Boolean,
                  val enableThread: Boolean,
                  val initialValue: Boolean,
                  val clear: Boolean,
                  override val viewType: ComposeStatusAdapter.ViewType = ComposeStatusAdapter.ViewType.EDITOR) : ComposeStatusAdapter.Item

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val statusInput = view.findById<TextInputEditText>(R.id.status_input)
    val statusCounter = view.findById<TextView>(R.id.status_counter)
    val keepDialogOpenSwitch = view.findById<SwitchCompat>(R.id.keep_dialog)
    val enableThreadSwitch = view.findById<SwitchCompat>(R.id.enable_thread)

    companion object {
      fun create(parent: ViewGroup): ViewHolder {
        val view = parent.inflate(R.layout.vh_editor)
        return ViewHolder(view)
      }
    }
  }
}