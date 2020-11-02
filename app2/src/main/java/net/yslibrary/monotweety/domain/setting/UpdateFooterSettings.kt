package net.yslibrary.monotweety.domain.setting

import net.yslibrary.monotweety.data.settings.SettingsRepository
import javax.inject.Inject

interface UpdateFooterSettings {
    suspend operator fun invoke(enabled: Boolean, text: String)
}

internal class UpdateFooterSettingsImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : UpdateFooterSettings {
    override suspend fun invoke(enabled: Boolean, text: String) {
        settingsRepository.updateFooter(enabled, text)
    }
}
