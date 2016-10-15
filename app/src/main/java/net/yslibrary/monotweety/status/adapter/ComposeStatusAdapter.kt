package net.yslibrary.monotweety.status.adapter

import android.support.v7.util.DiffUtil
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.twitter.sdk.android.core.models.Tweet
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by yshrsmz on 2016/10/13.
 */
class ComposeStatusAdapter(private val listener: Listener) : ListDelegationAdapter<List<ComposeStatusAdapter.Item>>() {

  var editorInitialized = false

  init {
    delegatesManager.addDelegate(PreviousStatusAdapterDelegate())

    delegatesManager.addDelegate(EditorAdapterDelegate(object : EditorAdapterDelegate.Listener {
      override fun onStatusChanged(status: String) {
        listener.onStatusChanged(status)
      }

      override fun onEnableThreadChanged(enabled: Boolean) {
        listener.onEnableThreadChanged(enabled)
      }

      override fun onKeepDialogOpenChanged(enabled: Boolean) {
        listener.onKeepDialogOpenChanged(enabled)
      }
    }))

    items = mutableListOf()
  }

  private fun editorItem(): EditorAdapterDelegate.Item {
    return items.last() as EditorAdapterDelegate.Item
  }

  fun setPreviousStatus(tweets: List<Tweet>) {
    val tweetItems = tweets.map {
      PreviousStatusAdapterDelegate.Item(
          id = it.id,
          status = it.text,
          createdAt = it.createdAt)
    }

    calculateDiff(items, tweetItems + items.last())
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
    change.subscribe {
      items = it.second
      it.first.dispatchUpdatesTo(this)
      editorInitialized = true
    }
  }

  fun updateEditor(item: EditorAdapterDelegate.Item) {
    updateEditorInternal(item.copy(initialValue = !editorInitialized))
  }

  fun clearEditor() {
    updateEditorInternal(editorItem()
        .copy(status = "",
            statusLength = 0,
            valid = false,
            initialValue = false,
            clear = true))
  }

  fun updateStatusCounter(valid: Boolean, length: Int, maxLength: Int) {
    updateEditorInternal(editorItem()
        .copy(valid = valid,
            statusLength = length,
            maxLength = maxLength))
  }

  fun calculateDiff(oldList: List<Item>, newList: List<Item>): Single<Pair<DiffUtil.DiffResult, List<Item>>> {
    return Single.fromCallable {
      DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
          val oldItem = oldList[oldItemPosition]
          val newItem = newList[newItemPosition]

          if (oldItem.viewType == newItem.viewType) {
            if (newItem.viewType == ComposeStatusAdapter.ViewType.EDITOR) {
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

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
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
        .map { Pair<DiffUtil.DiffResult, List<Item>>(it, newList) }
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
    fun onEnableThreadChanged(enabled: Boolean)
    fun onKeepDialogOpenChanged(enabled: Boolean)
  }
}