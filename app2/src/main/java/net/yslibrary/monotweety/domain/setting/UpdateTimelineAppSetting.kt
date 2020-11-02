package net.yslibrary.monotweety.domain.setting

import net.yslibrary.monotweety.data.settings.SettingsRepository
import javax.inject.Inject

interface UpdateTimelineAppSetting {
    suspend operator fun invoke(enabled: Boolean, packageName: String)
}

internal class UpdateTimelineAppSettingImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : UpdateTimelineAppSetting {
    override suspend fun invoke(enabled: Boolean, packageName: String) {
        settingsRepository.updateTimelineApp(enabled, packageName)
    }
}
