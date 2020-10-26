package net.yslibrary.monotweety.ui.settings.widget

import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.databinding.Item1lineTextBinding
import net.yslibrary.monotweety.ui.base.groupie.BindableItem
import net.yslibrary.monotweety.ui.base.setDebounceClickListener

class OneLineTextItem(
    private val item: Item,
    private val onClick: (item: Item) -> Unit,
) : BindableItem<Item1lineTextBinding>(R.layout.item_1line_text, Item1lineTextBinding::bind) {

    override fun bind(viewBinding: Item1lineTextBinding, position: Int) {
        viewBinding.apply {
            title.text = item.title
            root.isEnabled = item.enabled
            root.setDebounceClickListener { onClick(item) }
        }
    }

    data class Item(
        val title: String,
        val enabled: Boolean,
    )
}
