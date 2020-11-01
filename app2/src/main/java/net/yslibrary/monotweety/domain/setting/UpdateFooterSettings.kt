package net.yslibrary.monotweety.domain.setting

import net.yslibrary.monotweety.data.settings.SettingRepository
import javax.inject.Inject

interface UpdateFooterSettings {
    suspend operator fun invoke(enabled: Boolean, text: String)
}

internal class UpdateFooterSettingsImpl @Inject constructor(
    private val settingRepository: SettingRepository,
) : UpdateFooterSettings {
    override suspend fun invoke(enabled: Boolean, text: String) {
        settingRepository.updateFooter(enabled, text)
    }
}
