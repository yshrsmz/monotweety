package net.yslibrary.monotweety.status.adapter

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter

/**
 * Created by yshrsmz on 2016/10/13.
 */
class ComposeStatusAdapter : ListDelegationAdapter<List<ComposeStatusAdapter.Item>>() {

  interface Item {
    val viewType: ViewType
  }

  enum class ViewType {
    TWEET,
    EDITOR
  }
}