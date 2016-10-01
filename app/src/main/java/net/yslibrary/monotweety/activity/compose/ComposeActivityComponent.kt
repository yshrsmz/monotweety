package net.yslibrary.monotweety.activity.compose

import dagger.Component
import net.yslibrary.monotweety.UserComponent
import net.yslibrary.monotweety.activity.ActivityModule
import net.yslibrary.monotweety.base.di.ActivityScope

/**
 * Created by yshrsmz on 2016/10/01.
 */
@ActivityScope
@Component(
    dependencies = arrayOf(UserComponent::class),
    modules = arrayOf(ActivityModule::class)
)
interface ComposeActivityComponent : ActivityModule.Provider {
  fun inject(activity: ComposeActivity)
}