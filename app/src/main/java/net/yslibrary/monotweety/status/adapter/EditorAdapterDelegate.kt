package net.yslibrary.monotweety.status.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.disposables.SerialDisposable
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.base.inflate
import net.yslibrary.monotweety.base.setTo
import net.yslibrary.monotweety.base.showKeyboard

class EditorAdapterDelegate(
    private val listener: Listener
) : AdapterDelegate<List<ComposeStatusAdapter.Item>>() {

    val statusInputDisposable = SerialDisposable()

    override fun isForViewType(items: List<ComposeStatusAdapter.Item>, position: Int): Boolean {
        return items[position].viewType == ComposeStatusAdapter.ViewType.EDITOR
    }

    override fun onBindViewHolder(
        items: List<ComposeStatusAdapter.Item>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {

        if (holder is ViewHolder) {
            val item = items[position] as Item

            val shouldUpdateStatus =
                item.clear || item.initialValue || holder.statusInput.text.isNullOrBlank()

            if (shouldUpdateStatus) {
                holder.statusInput.setText(item.status, TextView.BufferType.EDITABLE)
            }
            val counterColor = if (item.valid) R.color.colorTextSecondary else R.color.red
            holder.statusCounter.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    counterColor
                )
            )
            holder.statusCounter.text = holder.itemView.context.getString(
                R.string.label_status_counter,
                item.statusLength,
                item.maxLength
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val holder = ViewHolder.create(parent)

        holder.statusInput.textChanges()
            .skip(1)
            .subscribe { listener.onStatusChanged(it.toString()) }
            .setTo(statusInputDisposable)

        holder.statusInput.post {
            holder.statusInput.showKeyboard()
        }

        return holder
    }

    data class Item(
        val status: String,
        val statusLength: Int,
        val maxLength: Int,
        val valid: Boolean,
        val clear: Boolean,
        val initialValue: Boolean = false,
        override val viewType: ComposeStatusAdapter.ViewType = ComposeStatusAdapter.ViewType.EDITOR
    ) : ComposeStatusAdapter.Item

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val statusInput = view.findById<TextInputEditText>(R.id.status_input)
        val statusCounter = view.findById<TextView>(R.id.status_counter)

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view = parent.inflate(R.layout.vh_editor)
                return ViewHolder(view)
            }
        }
    }

    interface Listener {
        fun onStatusChanged(status: String)
    }
}
