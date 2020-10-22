package net.yslibrary.monotweety

import android.content.Context
import com.codingfeline.twitter4kt.core.ConsumerKeys
import dagger.BindsInstance
import dagger.Component
import kotlinx.datetime.Clock
import net.yslibrary.monotweety.data.SingletonDataModule
import net.yslibrary.monotweety.domain.SingletonDomainModule
import net.yslibrary.monotweety.ui.AppUiSubcomponentModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SingletonDataModule::class,
        SingletonDomainModule::class,
        UserComponentModule::class,
        AppUiSubcomponentModule::class,
    ],
)
interface AppComponent : UserComponent.Provider,
    AppUiSubcomponentModule.ComponentProviders {

    fun inject(app: App)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance consumerKeys: ConsumerKeys,
            @BindsInstance clock: Clock,
        ): AppComponent
    }
}
