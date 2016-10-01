package net.yslibrary.monotweety.status

import android.support.v7.widget.Toolbar
import android.view.*
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.base.ActionBarController
import net.yslibrary.monotweety.base.findById
import timber.log.Timber

/**
 * Created by yshrsmz on 2016/10/01.
 */
class ComposeStatusController(private var status: String? = null) : ActionBarController() {

  init {
    setHasOptionsMenu(true)
  }

  override fun onCreate() {
    super.onCreate()
    Timber.d("status: $status")
  }

  override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = inflater.inflate(R.layout.controller_compose_status, container, false)

    return view
  }

  override fun onAttach(view: View) {
    super.onAttach(view)
    initToolbar()
  }

  fun initToolbar() {
    actionBar?.let {
      it.setDisplayHomeAsUpEnabled(true)
      activity.findById<Toolbar>(R.id.toolbar).navigationIcon = resources.getDrawable(R.drawable.ic_close_white_24dp, null)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)

    inflater.inflate(R.menu.menu_compose_status, menu)
  }

  override fun onPrepareOptionsMenu(menu: Menu) {
    super.onPrepareOptionsMenu(menu)

    // TODO: toggle depending on status text length
    menu.findItem(R.id.action_send_tweet)?.isEnabled = true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val id = item.itemId
    when (id) {
      R.id.action_send_tweet -> {
        Timber.d("option - action_send_tweet")
      }
      android.R.id.home -> {
        Timber.d("option - home")
        activity.onBackPressed()
      }
    }

    return super.onOptionsItemSelected(item)
  }

  override fun handleBack(): Boolean {
    return super.handleBack()
  }
}