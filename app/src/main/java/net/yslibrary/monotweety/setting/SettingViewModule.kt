package net.yslibrary.monotweety.setting

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.Config
import net.yslibrary.monotweety.Navigator
import net.yslibrary.monotweety.base.di.ControllerScope
import net.yslibrary.monotweety.base.di.Names
import net.yslibrary.monotweety.setting.domain.*
import net.yslibrary.monotweety.user.domain.GetUser
import net.yslibrary.rxeventbus.EventBus
import javax.inject.Named

/**
 * Created by yshrsmz on 2016/09/25.
 */
@Module
class SettingViewModule(private val activityBus: EventBus,
                        private val navigator: Navigator) {

  @ControllerScope
  @Provides
  @Named(Names.FOR_ACTIVITY)
  fun provideActivityBus(): EventBus = activityBus

  @ControllerScope
  @Provides
  fun provideNavigator(): Navigator = navigator

  @ControllerScope
  @Provides
  fun provideSettingViewModel(config: Config,
                              notificationEnabledManager: NotificationEnabledManager,
                              getUser: GetUser,
                              keepOpenManager: KeepOpenManager,
                              footerStateManager: FooterStateManager,
                              getInstalledSupportedApps: GetInstalledSupportedApps,
                              selectedTimelineAppInfoManager: SelectedTimelineAppInfoManager): SettingViewModel {
    return SettingViewModel(config,
        notificationEnabledManager,
        getUser,
        keepOpenManager,
        footerStateManager,
        getInstalledSupportedApps,
        selectedTimelineAppInfoManager)
  }

  interface DependencyProvider {
    @Named(Names.FOR_ACTIVITY)
    fun activityBus(): EventBus

    fun navigator(): Navigator
  }
}