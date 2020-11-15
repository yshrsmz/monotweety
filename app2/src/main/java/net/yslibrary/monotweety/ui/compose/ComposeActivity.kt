package net.yslibrary.monotweety.ui.compose

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class ComposeActivity : AppCompatActivity() {

    companion object {
        private const val KEY_STATUS = "key_status"

        fun callingIntent(context: Context, status: String? = null): Intent {
            return Intent(context.applicationContext, ComposeActivity::class.java)
                .apply {
                    if (!status.isNullOrBlank()) {
                        putExtra(KEY_STATUS, status)
                    }
                    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                            or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                            or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
        }
    }
}
