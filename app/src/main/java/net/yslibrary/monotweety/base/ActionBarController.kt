package net.yslibrary.monotweety.base

import androidx.appcompat.app.ActionBar
import android.view.MenuItem
import android.view.View
import net.yslibrary.monotweety.activity.ActionBarProvider

abstract class ActionBarController : BaseController() {

    open val title: String? = null

    open val shouldShowActionBar: Boolean = true

    open val hasOptionsMenu: Boolean = false

    open val hasBackButton: Boolean = false

    val actionBar: ActionBar?
        get() {
            val actionBarProvider = activity as ActionBarProvider?
            return actionBarProvider?.let { actionBarProvider.getSupportActionBar() }
        }

    override fun onAttach(view: View) {
        setHasOptionsMenu(shouldShowActionBar && (hasOptionsMenu || hasBackButton))
        if (shouldShowActionBar) {
            actionBar?.show()
            actionBar?.setDisplayHomeAsUpEnabled(hasBackButton)
        } else {
            actionBar?.hide()
        }
        setTitle()
        super.onAttach(view)
    }

    fun setTitle() {
        if (title != null) {
            actionBar?.title = title
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
