package net.yslibrary.monotweety.setting

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.ControllerScope
import net.yslibrary.monotweety.base.di.Names
import net.yslibrary.rxeventbus.EventBus
import javax.inject.Named

/**
 * Created by yshrsmz on 2016/09/25.
 */
@Module
class SettingViewModule(private val activityBus: EventBus) {

  @Provides
  @Named(Names.FOR_ACTIVITY)
  @ControllerScope
  fun provideActivityBus() = activityBus
}