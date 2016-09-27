package net.yslibrary.monotweety.event

import android.content.Intent
import net.yslibrary.rxeventbus.EventBus

/**
 * Created by yshrsmz on 2016/09/28.
 */
data class ActivityResult(val requestCode: Int, val resultCode: Int, val data: Intent?) : EventBus.Event