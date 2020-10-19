package net.yslibrary.monotweety

import dagger.Module
import dagger.Subcomponent
import net.yslibrary.monotweety.data.UserScopeDataModule
import net.yslibrary.monotweety.di.UserScope
import net.yslibrary.monotweety.domain.UserScopeDomainModule

@UserScope
@Subcomponent(
    modules = [
        UserScopeDataModule::class,
        UserScopeDomainModule::class,
    ]
)
interface UserComponent {

    @Subcomponent.Factory
    interface Factory {
        fun build(): UserComponent
    }

    interface Provider {
        fun userComponent(): UserComponent.Factory
    }
}

@Module(
    subcomponents = [
        UserComponent::class,
    ]
)
interface UserComponentModule {

}
