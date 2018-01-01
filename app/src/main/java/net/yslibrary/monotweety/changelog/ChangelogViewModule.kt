package net.yslibrary.monotweety.changelog

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.EventBus
import net.yslibrary.monotweety.base.di.ControllerScope
import net.yslibrary.monotweety.base.di.Names
import javax.inject.Named

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