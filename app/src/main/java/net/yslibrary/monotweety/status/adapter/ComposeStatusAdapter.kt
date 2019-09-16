package net.yslibrary.monotweety.status.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ComposeStatusAdapter(
    private val listener: Listener
) : ListDelegationAdapter<List<ComposeStatusAdapter.Item>>() {

    var editorInitialized = false

    init {

        delegatesManager.addDelegate(EditorAdapterDelegate(object : EditorAdapterDelegate.Listener {
            override fun onStatusChanged(status: String) {
                listener.onStatusChanged(status)
            }
        }))

        items = mutableListOf()
    }

    private fun editorItem(): EditorAdapterDelegate.Item {
        return items.last() as EditorAdapterDelegate.Item
    }

    fun updateEditorInternal(item: EditorAdapterDelegate.Item) {
        val change: Single<Pair<DiffUtil.DiffResult, List<Item>>>
        if (items.isEmpty() || items.last().viewType != ViewType.EDITOR) {
            // list is empty or last item is not editor
            change = calculateDiff(items, items + item)
        } else {
            // list item is not empty and last item is editor
            change = calculateDiff(items, items.dropLast(1) + item)
        }
        change.subscribeBy {
            synchronized(this, {
                Timber.d("update editor")
                items = it.second
                it.first.dispatchUpdatesTo(this)
                editorInitialized = true
            })
        }
    }

    fun updateEditor(item: EditorAdapterDelegate.Item) {
        updateEditorInternal(item.copy(initialValue = !editorInitialized))
    }

    fun calculateDiff(
        oldList: List<Item>,
        newList: List<Item>
    ): Single<Pair<DiffUtil.DiffResult, List<Item>>> {
        return Single.fromCallable {
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = oldList[oldItemPosition]
                    val newItem = newList[newItemPosition]

                    if (oldItem.viewType == newItem.viewType) {
                        if (newItem.viewType == ViewType.EDITOR) {
                            return true
                        } else if (oldItem is PreviousStatusAdapterDelegate.Item && newItem is PreviousStatusAdapterDelegate.Item) {
                            return oldItem.id == newItem.id
                        }
                    }

                    return false
                }

                override fun getOldListSize(): Int {
                    return oldList.size
                }

                override fun getNewListSize(): Int {
                    return newList.size
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val oldItem = oldList[oldItemPosition]
                    val newItem = newList[newItemPosition]

                    if (oldItem.viewType == newItem.viewType) {
                        if (oldItem is EditorAdapterDelegate.Item && newItem is EditorAdapterDelegate.Item) {
                            return oldItem == newItem
                        } else if (oldItem is PreviousStatusAdapterDelegate.Item && newItem is PreviousStatusAdapterDelegate.Item) {
                            return oldItem.id == newItem.id
                        }
                    }
                    return false
                }
            })
        }
            .map { Pair(it, newList) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
    }

    interface Item {
        val viewType: ViewType
    }

    enum class ViewType {
        PREVIOUS_STATUS,
        EDITOR
    }

    interface Listener {
        fun onStatusChanged(status: String)
    }
}
