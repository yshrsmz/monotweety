package net.yslibrary.monotweety.domain.setting

import net.yslibrary.monotweety.data.setting.SettingRepository
import javax.inject.Inject

interface UpdateTimelineAppSetting {
    suspend operator fun invoke(enabled: Boolean, packageName: String)
}

internal class UpdateTimelineAppSettingImpl @Inject constructor(
    private val settingRepository: SettingRepository,
) : UpdateTimelineAppSetting {
    override suspend fun invoke(enabled: Boolean, packageName: String) {
        settingRepository.updateTimelineApp(enabled, packageName)
    }
}
