package net.yslibrary.monotweety

import android.content.Context
import com.codingfeline.twitter4kt.core.ConsumerKeys
import dagger.BindsInstance
import dagger.Component
import net.yslibrary.monotweety.data.SingletonDataModule
import net.yslibrary.monotweety.domain.SingletonDomainModule
import net.yslibrary.monotweety.ui.launcher.LauncherActivityComponent
import net.yslibrary.monotweety.ui.launcher.LauncherActivityComponentModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SingletonDataModule::class,
        SingletonDomainModule::class,
        UserComponentModule::class,
        LauncherActivityComponentModule::class,
    ],
)
interface AppComponent : UserComponent.Provider,
    LauncherActivityComponent.ComponentProvider {

    fun inject(app: App)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance consumerKeys: ConsumerKeys,
        ): AppComponent
    }
}
