package net.yslibrary.monotweety.event

import android.content.Intent
import net.yslibrary.rxeventbus.EventBus

/**
 * Created by yshrsmz on 2016/09/28.
 */
data class NewIntent(val intent: Intent) : EventBus.Event