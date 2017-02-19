package net.yslibrary.monotweety.activity

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.Navigator
import net.yslibrary.monotweety.base.BaseActivity
import net.yslibrary.monotweety.base.di.ActivityScope
import net.yslibrary.monotweety.base.di.Names
import net.yslibrary.rxeventbus.EventBus
import javax.inject.Named

@Module
class ActivityModule(private val activity: BaseActivity) {

  @Provides
  @Named(Names.FOR_ACTIVITY)
  @ActivityScope
  fun provideActivityBus(): EventBus = EventBus()

  @Provides
  @ActivityScope
  fun provideNavigator(): Navigator {
    return Navigator(activity)
  }

  interface Provider {
    @Named(Names.FOR_ACTIVITY)
    fun activityBus(): EventBus

    fun navigator(): Navigator
  }
}