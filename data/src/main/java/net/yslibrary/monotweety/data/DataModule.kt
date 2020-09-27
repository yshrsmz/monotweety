package net.yslibrary.monotweety.data

import dagger.Module
import net.yslibrary.monotweety.data.session.SessionDataModule
import net.yslibrary.monotweety.data.setting.SettingDataModule
import net.yslibrary.monotweety.data.user.UserDataModule

@Module(
    includes = [
        SessionDataModule::class,
        SettingDataModule::class,
        UserDataModule::class,
    ]
)
interface DataModule {
}