package net.yslibrary.monotweety.quicksetting

import android.annotation.TargetApi
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast
import net.yslibrary.monotweety.App
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.activity.compose.ComposeActivity
import net.yslibrary.monotweety.activity.main.MainActivity
import net.yslibrary.monotweety.base.closeNotificationDrawer

/**
 * Created by yshrsmz on 2016/11/17.
 */
@TargetApi(Build.VERSION_CODES.N)
class EditorTileService : TileService() {

  companion object {
    fun callingIntent(context: Context): Intent {
      return Intent(context, EditorTileService::class.java)
    }
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    return Service.START_STICKY
  }

  override fun onClick() {
    super.onClick()
    App.appComponent(this).isLoggedIn().execute()
        .subscribe {
          closeNotificationDrawer()
          if (it) {
            startActivity(ComposeActivity.callingIntent(this))
          } else {
            startActivity(MainActivity.callingIntent(this))
            Toast.makeText(this, R.string.error_not_authorized, Toast.LENGTH_SHORT).show()
          }
        }
  }

  override fun onStartListening() {
    super.onStartListening()
    qsTile.state = Tile.STATE_ACTIVE
    qsTile.updateTile()
  }
}