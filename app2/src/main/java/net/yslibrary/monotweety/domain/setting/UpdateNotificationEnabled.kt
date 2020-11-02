package net.yslibrary.monotweety.domain.setting

import net.yslibrary.monotweety.data.settings.SettingsRepository
import javax.inject.Inject

interface UpdateNotificationEnabled {
    suspend operator fun invoke(enabled: Boolean)
}

internal class UpdateNotificationEnabledImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : UpdateNotificationEnabled {
    override suspend fun invoke(enabled: Boolean) {
        settingsRepository.updateNotificationEnabled(enabled)
    }
}
