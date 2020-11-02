package net.yslibrary.monotweety.domain.setting

import kotlinx.coroutines.flow.Flow
import net.yslibrary.monotweety.data.settings.Settings
import net.yslibrary.monotweety.data.settings.SettingsRepository
import javax.inject.Inject

interface ObserveSettings {
    operator fun invoke(): Flow<Settings>
}

internal class ObserveSettingsImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ObserveSettings {
    override fun invoke(): Flow<Settings> {
        return settingsRepository.settingsFlow
    }
}
