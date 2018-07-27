package net.yslibrary.monotweety.status.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.base.inflate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class PreviousStatusAdapterDelegate : AdapterDelegate<List<ComposeStatusAdapter.Item>>() {

    private val createdAtRawFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH)
    private val createdAtFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")

    override fun onBindViewHolder(items: List<ComposeStatusAdapter.Item>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) {

        if (holder is ViewHolder) {
            val item = items[position] as Item

            holder.status.text = item.status
            holder.timestamp.text = getFormattedCreatedAt(item.createdAt)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun isForViewType(items: List<ComposeStatusAdapter.Item>, position: Int): Boolean {
        return items[position].viewType == ComposeStatusAdapter.ViewType.PREVIOUS_STATUS
    }

    fun getFormattedCreatedAt(rawDate: String): String {
        val createdAtDate = ZonedDateTime.parse(rawDate, createdAtRawFormatter)
            .withZoneSameInstant(ZoneId.systemDefault())
        return createdAtDate.format(createdAtFormatter)
    }

    data class Item(val id: Long,
                    val status: String,
                    val createdAt: String,
                    override val viewType: ComposeStatusAdapter.ViewType = ComposeStatusAdapter.ViewType.PREVIOUS_STATUS) : ComposeStatusAdapter.Item

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val status = view.findById<TextView>(R.id.status)
        val timestamp = view.findById<TextView>(R.id.timestamp)

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view = parent.inflate(R.layout.vh_previous_status)
                return ViewHolder(view)
            }
        }
    }
}
