package net.yslibrary.monotweety.domain.setting

import kotlinx.coroutines.flow.Flow
import net.yslibrary.monotweety.data.setting.Setting
import net.yslibrary.monotweety.data.setting.SettingRepository
import javax.inject.Inject

interface ObserveSetting {
    operator fun invoke(): Flow<Setting>
}

internal class ObserveSettingImpl @Inject constructor(
    private val settingRepository: SettingRepository
) : ObserveSetting {
    override fun invoke(): Flow<Setting> {
        return settingRepository.settingFlow
    }
}
