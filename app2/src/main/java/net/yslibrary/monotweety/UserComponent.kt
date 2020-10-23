package net.yslibrary.monotweety

import dagger.Module
import dagger.Subcomponent
import net.yslibrary.monotweety.data.UserScopeDataModule
import net.yslibrary.monotweety.di.UserScope
import net.yslibrary.monotweety.domain.UserScopeDomainModule
import net.yslibrary.monotweety.ui.UserUiSubcomponentModule

@UserScope
@Subcomponent(
    modules = [
        UserScopeDataModule::class,
        UserScopeDomainModule::class,
        UserUiSubcomponentModule::class,
    ]
)
interface UserComponent : UserUiSubcomponentModule.ComponentProviders {

    @Subcomponent.Factory
    interface Factory {
        fun build(): UserComponent
    }

    interface Provider {
        fun userComponent(): Factory
    }
}

@Module(
    subcomponents = [
        UserComponent::class,
    ]
)
interface UserComponentModule {

}
