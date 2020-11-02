package net.yslibrary.monotweety.domain

import dagger.Binds
import dagger.Module
import net.yslibrary.monotweety.domain.appinfo.GetInstalledTwitterApps
import net.yslibrary.monotweety.domain.appinfo.GetInstalledTwitterAppsImpl
import net.yslibrary.monotweety.domain.session.ObserveSession
import net.yslibrary.monotweety.domain.session.ObserveSessionImpl
import net.yslibrary.monotweety.domain.setting.GetTimelineTwitterApp
import net.yslibrary.monotweety.domain.setting.GetTimelineTwitterAppImpl
import net.yslibrary.monotweety.domain.setting.ObserveSettings
import net.yslibrary.monotweety.domain.setting.ObserveSettingsImpl
import net.yslibrary.monotweety.domain.setting.UpdateFooterSettings
import net.yslibrary.monotweety.domain.setting.UpdateFooterSettingsImpl
import net.yslibrary.monotweety.domain.setting.UpdateNotificationEnabled
import net.yslibrary.monotweety.domain.setting.UpdateNotificationEnabledImpl
import net.yslibrary.monotweety.domain.setting.UpdateTimelineAppSetting
import net.yslibrary.monotweety.domain.setting.UpdateTimelineAppSettingImpl

@Module
internal interface SingletonDomainModule {
    @Binds
    fun bindObserveSetting(impl: ObserveSettingsImpl): ObserveSettings

    @Binds
    fun bindUpdateFooterSettings(impl: UpdateFooterSettingsImpl): UpdateFooterSettings

    @Binds
    fun bindUpdateTimelineAppSetting(impl: UpdateTimelineAppSettingImpl): UpdateTimelineAppSetting

    @Binds
    fun bindUpdateNotificationEnabled(impl: UpdateNotificationEnabledImpl): UpdateNotificationEnabled

    @Binds
    fun bindObserveSession(impl: ObserveSessionImpl): ObserveSession

    @Binds
    fun bindGetInstalledTwitterApps(impl: GetInstalledTwitterAppsImpl): GetInstalledTwitterApps

    @Binds
    fun bindGetTimelineTwitterApp(impl: GetTimelineTwitterAppImpl): GetTimelineTwitterApp
}
