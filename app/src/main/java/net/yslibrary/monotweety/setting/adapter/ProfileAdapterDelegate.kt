package net.yslibrary.monotweety.setting.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.findById
import net.yslibrary.monotweety.base.load
import net.yslibrary.monotweety.data.user.User

class ProfileAdapterDelegate(private val listener: Listener) : AdapterDelegate<List<SettingAdapter.Item>>() {
    override fun isForViewType(items: List<SettingAdapter.Item>, position: Int): Boolean {
        return items[position] is Item
    }

    override fun onBindViewHolder(items: List<SettingAdapter.Item>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any?>) {
        val item = items[position] as Item
        if (holder is ViewHolder) {
            val context = holder.itemView.context
            if (item.loading) {
                holder.name.text = context.getString(R.string.label_loading)
                holder.screenName.text = context.getString(R.string.label_loading)
                holder.thumb.setImageResource(android.R.color.transparent)
                holder.logoutButton.setOnClickListener(null)
                holder.openProfileButton.setOnClickListener(null)
            } else {
                holder.name.text = item.name
                holder.screenName.text = item.screenName
                holder.thumb.load(item.imageUrl)
                holder.logoutButton.setOnClickListener { listener.onLogoutClick() }
                holder.openProfileButton.setOnClickListener { listener.onOpenProfileClick() }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder.create(parent)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val thumb = view.findById<ImageView>(R.id.thumb)
        val name = view.findById<TextView>(R.id.name)
        val screenName = view.findById<TextView>(R.id.screen_name)

        val logoutButton = view.findById<Button>(R.id.logout)
        val openProfileButton = view.findById<Button>(R.id.open_profile)

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.vh_profile, parent, false)
                return ViewHolder(view)
            }
        }
    }

    data class Item(val name: String,
                    val screenName: String,
                    val imageUrl: String,
                    val enabled: Boolean = true,
                    val loading: Boolean = false,
                    override val type: SettingAdapter.ViewType) : SettingAdapter.Item {
        companion object {
            fun empty(): Item {
                return Item("", "", "", false, true, SettingAdapter.ViewType.PROFILE)
            }

            fun from(user: User): Item {
                return Item(
                    name = user.name,
                    screenName = user.screenName,
                    imageUrl = user.profileImageUrl,
                    enabled = true,
                    loading = false,
                    type = SettingAdapter.ViewType.PROFILE)
            }
        }
    }

    interface Listener {
        fun onOpenProfileClick()
        fun onLogoutClick()
    }
}
