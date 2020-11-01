package net.yslibrary.monotweety.ui.settings.widget

import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.databinding.Item2lineTextBinding
import net.yslibrary.monotweety.ui.base.groupie.BindableItem
import net.yslibrary.monotweety.ui.base.setDebounceClickListener

class TwoLineTextItem(
    private val item: Item,
    private val onClick: (item: Item) -> Unit,
) : BindableItem<Item2lineTextBinding>(
    R.layout.item_2line_text,
    Item2lineTextBinding::bind
) {
    override fun bind(viewBinding: Item2lineTextBinding, position: Int) {
        viewBinding.apply {
            title.text = item.title
            subtitle.text = item.subTitle
            root.isEnabled = item.enabled
            root.setDebounceClickListener { onClick(item) }
        }
    }

    override fun hasSameContentAs(other: com.xwray.groupie.Item<*>): Boolean {
        if (other !is TwoLineTextItem) return false
        return other.item == item
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>): Boolean {
        if (other !is TwoLineTextItem) return false
        return other.item.title == item.title
    }

    data class Item(
        val title: String,
        val subTitle: String,
        val enabled: Boolean,
    )
}
