package net.yslibrary.monotweety.event

import android.content.Intent
import net.yslibrary.monotweety.base.EventBus

data class NewIntent(val intent: Intent) : EventBus.Event