package net.yslibrary.monotweety.activity

import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.ActivityScope
import net.yslibrary.rxeventbus.EventBus

/**
 * Created by yshrsmz on 2016/09/24.
 */
@Module
class ActivityModule(protected val activity: AppCompatActivity) {

  @Provides
  @ActivityScope
  fun provideActivityBus(): EventBus = EventBus()
}