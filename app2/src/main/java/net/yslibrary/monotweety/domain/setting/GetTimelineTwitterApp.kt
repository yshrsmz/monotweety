package net.yslibrary.monotweety.domain.setting

import kotlinx.coroutines.flow.firstOrNull
import net.yslibrary.monotweety.data.appinfo.AppInfo
import net.yslibrary.monotweety.data.appinfo.TwitterApp
import net.yslibrary.monotweety.data.appinfo.TwitterAppRepository
import net.yslibrary.monotweety.data.settings.SettingsRepository
import javax.inject.Inject

interface GetTimelineTwitterApp {
    suspend operator fun invoke(): AppInfo?
}

internal class GetTimelineTwitterAppImpl @Inject constructor(
    private val twitterAppRepository: TwitterAppRepository,
    private val settingsRepository: SettingsRepository,
) : GetTimelineTwitterApp {

    override suspend fun invoke(): AppInfo? {
        val settings = settingsRepository.settingsFlow.firstOrNull() ?: return null
        val twitterApp = TwitterApp.fromPackageName(settings.timelineAppPackageName)
            .takeUnless { it == TwitterApp.NONE } ?: return null

        return twitterAppRepository.getByPackageName(twitterApp)
    }
}
