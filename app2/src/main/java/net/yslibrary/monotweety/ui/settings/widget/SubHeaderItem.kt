package net.yslibrary.monotweety.ui.settings.widget

import com.xwray.groupie.Item
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.databinding.ItemSubheaderBinding
import net.yslibrary.monotweety.ui.base.groupie.BindableItem

class SubHeaderItem(
    val title: String,
) : BindableItem<ItemSubheaderBinding>(
    R.layout.item_subheader,
    ItemSubheaderBinding::bind,
) {
    override fun bind(viewBinding: ItemSubheaderBinding, position: Int) {
        viewBinding.title.text = title
    }

    override fun hasSameContentAs(other: Item<*>): Boolean {
        if (other !is SubHeaderItem) return false
        return other.title == title
    }

    override fun isSameAs(other: Item<*>): Boolean {
        if (other !is SubHeaderItem) return false
        return other.title == title
    }
}
