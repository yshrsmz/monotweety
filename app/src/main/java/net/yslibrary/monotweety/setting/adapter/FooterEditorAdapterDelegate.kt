package net.yslibrary.monotweety.setting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.base.inflate
import timber.log.Timber

class FooterEditorAdapterDelegate(
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
            val context = holder.itemView.context
            val res = context.resources

            val subTitle = if (item.checked)
                res.getString(R.string.sub_label_footer_on, item.footerText)
            else
                res.getString(R.string.sub_label_footer_off)

            holder.title.text = res.getString(R.string.label_footer)
            holder.subTitle.text = subTitle
            holder.itemView.isEnabled = item.enabled
            holder.itemView.setOnClickListener { onClick(context, item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder.create(parent)
    }

    private fun onClick(context: Context, item: Item) {
        // show dialog
        val view = context.inflate(R.layout.dialog_footer_editor)
        val enabledSwitch = view.findById<SwitchCompat>(R.id.switch_button)
        val input = view.findById<EditText>(R.id.input)

        enabledSwitch.isChecked = item.checked
        input.setText(item.footerText, TextView.BufferType.EDITABLE)

        input.isEnabled = item.checked
        enabledSwitch.setOnCheckedChangeListener { _, checked -> input.isEnabled = checked }

        Timber.tag("Dialog").i("onClick - Edit Footer")
        AlertDialog.Builder(context)
            .setTitle(R.string.title_edit_footer)
            .setView(view)
            .setPositiveButton(R.string.label_confirm,
                { _, _ ->
                    val enabled = enabledSwitch.isChecked
                    val footerText = input.text.toString()
                    listener.onFooterUpdated(enabled, footerText)
                }).show()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title = view.findById<TextView>(R.id.title)
        val subTitle = view.findById<TextView>(R.id.sub_title)

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.vh_2line_text, parent, false)
                return ViewHolder(view)
            }
        }
    }

    data class Item(
        val enabled: Boolean,
        val checked: Boolean,
        val footerText: String,
        override val type: SettingAdapter.ViewType
    ) : SettingAdapter.Item

    interface Listener {
        fun onFooterUpdated(enabled: Boolean, footerText: String)
    }
}
