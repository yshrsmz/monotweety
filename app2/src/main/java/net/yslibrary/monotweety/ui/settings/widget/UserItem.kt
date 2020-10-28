package net.yslibrary.monotweety.ui.settings.widget

import coil.load
import com.xwray.groupie.Item
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.data.user.User
import net.yslibrary.monotweety.databinding.ItemUserBinding
import net.yslibrary.monotweety.ui.base.groupie.BindableItem

class UserItem(
    val user: User?,
) : BindableItem<ItemUserBinding>(
    R.layout.item_user,
    ItemUserBinding::bind
) {
    override fun bind(viewBinding: ItemUserBinding, position: Int) {
        val context = viewBinding.root.context
        if (user == null) {
            viewBinding.userImage.load(R.drawable.ic_baseline_account_circle_24)
            viewBinding.userName.text = context.getString(R.string.loading)
            viewBinding.userScreenName.text = ""
        } else {
            viewBinding.userImage.load(user.profileImageUrl) {
                this.placeholder(R.drawable.ic_baseline_account_circle_24)
            }
            viewBinding.userName.text = user.name
            viewBinding.userScreenName.text =
                context.getString(R.string.screen_name, user.screenName)
        }
    }

    override fun hasSameContentAs(other: Item<*>): Boolean {
        if (other !is UserItem) return false
        return other.user == user
    }

    override fun isSameAs(other: Item<*>): Boolean {
        if (other !is UserItem) return false
        return other.user?.id == user?.id
    }
}
