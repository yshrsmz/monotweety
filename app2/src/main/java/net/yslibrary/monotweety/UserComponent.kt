package net.yslibrary.monotweety

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
}
