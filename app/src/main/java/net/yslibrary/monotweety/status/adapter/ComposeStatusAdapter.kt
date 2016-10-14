package net.yslibrary.monotweety.status.adapter

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter

/**
 * Created by yshrsmz on 2016/10/13.
 */
class ComposeStatusAdapter(private val listener: Listener) : ListDelegationAdapter<List<ComposeStatusAdapter.Item>>() {

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