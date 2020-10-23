package net.yslibrary.monotweety.data.settings

data class Settings(
    val notificationEnabled: Boolean,
    val footerEnabled: Boolean,
    val footerText: String,
    val timelineAppEnabled: Boolean,
    val timelineAppPackageName: String,
)
