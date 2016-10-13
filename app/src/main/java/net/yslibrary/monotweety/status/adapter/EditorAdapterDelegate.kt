package net.yslibrary.monotweety.status.adapter

import android.support.design.widget.TextInputEditText
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
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

  }

  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
    return ViewHolder.create(parent)
  }

  data class Item(val status: String,
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