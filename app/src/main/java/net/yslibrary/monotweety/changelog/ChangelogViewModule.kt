package net.yslibrary.monotweety.changelog

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.ControllerScope
import net.yslibrary.monotweety.base.di.Names
import net.yslibrary.rxeventbus.EventBus
import javax.inject.Named

/**
 * Created by yshrsmz on 2016/11/04.
 */
@Module
class ChangelogViewModule(private val activityBus: EventBus) {

  @ControllerScope
  @Provides
  @Named(Names.FOR_ACTIVITY)
  fun provideActivityBus(): EventBus = activityBus

  interface DependencyProvider {
    @Named(Names.FOR_ACTIVITY)
    fun activityBus(): EventBus
  }
}