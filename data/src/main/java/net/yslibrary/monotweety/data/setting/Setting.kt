package net.yslibrary.monotweety.data.setting

data class Setting(
    val notificationEnabled: Boolean,
    val footerEnabled: Boolean,
    val footerText: String,
    val timelineAppEnabled: Boolean,
    val timelineAppPackageName: String,
)