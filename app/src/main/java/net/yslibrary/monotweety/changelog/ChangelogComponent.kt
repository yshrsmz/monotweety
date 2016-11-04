package net.yslibrary.monotweety.changelog

import dagger.Component
import net.yslibrary.monotweety.UserComponent
import net.yslibrary.monotweety.base.di.ControllerScope

/**
 * Created by yshrsmz on 2016/11/04.
 */
@ControllerScope
@Component(
    dependencies = arrayOf(UserComponent::class),
    modules = arrayOf(ChangelogViewModule::class)
)
interface ChangelogComponent {
  fun inject(controller: ChangelogController)
}