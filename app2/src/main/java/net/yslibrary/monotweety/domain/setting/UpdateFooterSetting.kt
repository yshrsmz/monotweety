package net.yslibrary.monotweety.domain.setting

import net.yslibrary.monotweety.data.setting.SettingRepository
import javax.inject.Inject

interface UpdateFooterSetting {
    suspend operator fun invoke(enabled: Boolean, text: String)
}

internal class UpdateFooterSettingImpl @Inject constructor(
    private val settingRepository: SettingRepository
) : UpdateFooterSetting {
    override suspend fun invoke(enabled: Boolean, text: String) {
        settingRepository.updateFooter(enabled, text)
    }
}
