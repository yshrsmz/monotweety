package net.yslibrary.monotweety.status.adapter

import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.jakewharton.rxbinding2.widget.checkedChanges
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.disposables.SerialDisposable
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.base.inflate
import net.yslibrary.monotweety.base.setTo

class EditorAdapterDelegate(private val listener: Listener) : AdapterDelegate<List<ComposeStatusAdapter.Item>>() {

    val statusInputDisposable = SerialDisposable()
    val enableThreadSwitchDisposable = SerialDisposable()
    val keepOpenDisposable = SerialDisposable()

    override fun isForViewType(items: List<ComposeStatusAdapter.Item>, position: Int): Boolean {
        return items[position].viewType == ComposeStatusAdapter.ViewType.EDITOR
    }

    override fun onBindViewHolder(items: List<ComposeStatusAdapter.Item>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) {

        if (holder is ViewHolder) {
            val item = items[position] as Item

            val shouldUpdateStatus = item.clear || item.initialValue || holder.statusInput.text.isBlank()

            if (shouldUpdateStatus) {
                holder.statusInput.setText(item.status, TextView.BufferType.EDITABLE)
            }
            val counterColor = if (item.valid) R.color.colorTextSecondary else R.color.red
            holder.statusCounter.setTextColor(ContextCompat.getColor(holder.itemView.context, counterColor))
            holder.statusCounter.text = holder.itemView.context.getString(R.string.label_status_counter, item.statusLength, item.maxLength)

            // update only if value is updated at somewhere
            if (item.keepOpen != holder.keepOpenSwitch.isChecked) {
                holder.keepOpenSwitch.isChecked = item.keepOpen
            }

            if (item.enableThread != holder.enableThreadSwitch.isChecked) {
                holder.enableThreadSwitch.isChecked = item.enableThread
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val holder = ViewHolder.create(parent)

        holder.statusInput.textChanges()
            .skip(1)
            .subscribe { listener.onStatusChanged(it.toString()) }
            .setTo(statusInputDisposable)

        holder.enableThreadSwitch.checkedChanges()
            .subscribe { listener.onEnableThreadChanged(it) }
            .setTo(enableThreadSwitchDisposable)

        holder.keepOpenSwitch.checkedChanges()
            .subscribe { listener.onKeepOpenChanged(it) }
            .setTo(keepOpenDisposable)

        return holder
    }

    data class Item(val status: String,
                    val statusLength: Int,
                    val maxLength: Int,
                    val valid: Boolean,
                    val keepOpen: Boolean,
                    val enableThread: Boolean,
                    val clear: Boolean,
                    val initialValue: Boolean = false,
                    override val viewType: ComposeStatusAdapter.ViewType = ComposeStatusAdapter.ViewType.EDITOR) : ComposeStatusAdapter.Item

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val statusInput = view.findById<TextInputEditText>(R.id.status_input)
        val statusCounter = view.findById<TextView>(R.id.status_counter)
        val keepOpenSwitch = view.findById<SwitchCompat>(R.id.keep_open)
        val enableThreadSwitch = view.findById<SwitchCompat>(R.id.enable_thread)

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view = parent.inflate(R.layout.vh_editor)
                return ViewHolder(view)
            }
        }
    }

    interface Listener {
        fun onStatusChanged(status: String)
        fun onEnableThreadChanged(enabled: Boolean)
        fun onKeepOpenChanged(enabled: Boolean)
    }
}
