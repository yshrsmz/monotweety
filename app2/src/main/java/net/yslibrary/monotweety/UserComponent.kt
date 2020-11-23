package net.yslibrary.monotweety

import com.codingfeline.twitter4kt.core.model.oauth1a.AccessToken
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import net.yslibrary.monotweety.data.UserScopeDataModule
import net.yslibrary.monotweety.di.UserScope
import net.yslibrary.monotweety.domain.UserScopeDomainModule
import net.yslibrary.monotweety.notification.NotificationServiceComponent
import net.yslibrary.monotweety.ui.UserUiSubcomponentModule
import net.yslibrary.monotweety.ui.compose.ComposeActivityComponent

@UserScope
@Subcomponent(
    modules = [
        UserScopeDataModule::class,
        UserScopeDomainModule::class,
        UserSubcomponentModule::class,
    ]
)
interface UserComponent : UserSubcomponentModule.ComponentProviders {

    @Subcomponent.Factory
    interface Factory {
        fun build(@BindsInstance accessToken: AccessToken): UserComponent
    }

    interface ComponentProvider {
        fun userComponent(): Factory
    }
}

@Module(
    includes = [
        UserUiSubcomponentModule::class,
    ],
    subcomponents = [
        NotificationServiceComponent::class,
        ComposeActivityComponent::class,
    ]
)
interface UserSubcomponentModule {
    interface ComponentProviders : UserUiSubcomponentModule.ComponentProviders,
        NotificationServiceComponent.ComponentProvider,
        ComposeActivityComponent.ComponentProvider

}

@Module(
    subcomponents = [
        UserComponent::class,
    ]
)
interface UserComponentModule {

}
