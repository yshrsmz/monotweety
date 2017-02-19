package net.yslibrary.monotweety.event

import android.content.Intent
import net.yslibrary.rxeventbus.EventBus

data class NewIntent(val intent: Intent) : EventBus.Event