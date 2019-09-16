package net.yslibrary.monotweety.setting

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.Config
import net.yslibrary.monotweety.Navigator
import net.yslibrary.monotweety.base.EventBus
import net.yslibrary.monotweety.base.di.ControllerScope
import net.yslibrary.monotweety.base.di.Names
import net.yslibrary.monotweety.setting.domain.FooterStateManager
import net.yslibrary.monotweety.setting.domain.GetInstalledSupportedApps
import net.yslibrary.monotweety.setting.domain.NotificationEnabledManager
import net.yslibrary.monotweety.setting.domain.SelectedTimelineAppInfoManager
import net.yslibrary.monotweety.user.domain.GetUser
import javax.inject.Named

@Module
class SettingViewModule(
    private val activityBus: EventBus,
    private val navigator: Navigator
) {

    @ControllerScope
    @Provides
    @Named(Names.FOR_ACTIVITY)
    fun provideActivityBus(): EventBus = activityBus

    @ControllerScope
    @Provides
    fun provideNavigator(): Navigator = navigator

    @ControllerScope
    @Provides
    fun provideSettingViewModel(
        config: Config,
        notificationEnabledManager: NotificationEnabledManager,
        getUser: GetUser,
        footerStateManager: FooterStateManager,
        getInstalledSupportedApps: GetInstalledSupportedApps,
        selectedTimelineAppInfoManager: SelectedTimelineAppInfoManager
    ): SettingViewModel {
        return SettingViewModel(
            config,
            notificationEnabledManager,
            getUser,
            footerStateManager,
            getInstalledSupportedApps,
            selectedTimelineAppInfoManager
        )
    }

    interface DependencyProvider {
        @Named(Names.FOR_ACTIVITY)
        fun activityBus(): EventBus

        fun navigator(): Navigator
    }
}
