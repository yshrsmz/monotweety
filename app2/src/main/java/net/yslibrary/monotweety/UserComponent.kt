package net.yslibrary.monotweety

import com.codingfeline.twitter4kt.core.model.oauth1a.AccessToken
import dagger.BindsInstance
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
        fun build(@BindsInstance accessToken: AccessToken): UserComponent
    }

    interface ComponentProvider {
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
