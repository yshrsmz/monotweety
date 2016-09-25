package net.yslibrary.monotweety.activity

import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.di.ActivityScope
import net.yslibrary.monotweety.base.di.Names
import net.yslibrary.rxeventbus.EventBus
import javax.inject.Named

/**
 * Created by yshrsmz on 2016/09/24.
 */
@Module
class ActivityModule(protected val activity: AppCompatActivity) {

  @Provides
  @Named(Names.FOR_ACTIVITY)
  @ActivityScope
  fun provideActivityBus(): EventBus = EventBus()

  interface Provider {
    @Named(Names.FOR_ACTIVITY)
    fun activityBus(): EventBus
  }
}