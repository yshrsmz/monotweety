package net.yslibrary.monotweety.ui.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import net.yslibrary.monotweety.R

class ComposeActivity : AppCompatActivity(R.layout.activity_compose) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MaterialAlertDialogBuilder(this)
            .setTitle("Title")
            .setMessage("Message")
            .setOnDismissListener { finish() }
            .setNegativeButton(R.string.cancel) { dialog, which -> /* no-op */ }
            .setPositiveButton(R.string.tweet) { dialog, which -> /* no-op */ }
            .show()
    }

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
