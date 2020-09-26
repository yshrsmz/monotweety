package net.yslibrary.monotweety.changelog

import dagger.Component
import net.yslibrary.monotweety.UserComponent
import net.yslibrary.monotweety.base.di.ControllerScope

@ControllerScope
@Component(
    dependencies = [UserComponent::class],
    modules = [ChangelogViewModule::class]
)
interface ChangelogComponent {
    fun inject(controller: ChangelogController)
}
