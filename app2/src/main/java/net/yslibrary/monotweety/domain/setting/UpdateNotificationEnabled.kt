package net.yslibrary.monotweety.domain.setting

import net.yslibrary.monotweety.data.settings.SettingRepository
import javax.inject.Inject

interface UpdateNotificationEnabled {
    suspend operator fun invoke(enabled: Boolean)
}

internal class UpdateNotificationEnabledImpl @Inject constructor(
    private val settingRepository: SettingRepository,
) : UpdateNotificationEnabled {
    override suspend fun invoke(enabled: Boolean) {
        settingRepository.updateNotificationEnabled(enabled)
    }
}
