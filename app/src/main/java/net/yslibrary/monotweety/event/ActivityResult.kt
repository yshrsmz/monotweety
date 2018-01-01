package net.yslibrary.monotweety.event

import android.content.Intent
import net.yslibrary.monotweety.base.EventBus

data class ActivityResult(val requestCode: Int, val resultCode: Int, val data: Intent?) : EventBus.Event