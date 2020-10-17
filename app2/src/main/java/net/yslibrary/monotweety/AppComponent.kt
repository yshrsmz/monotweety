package net.yslibrary.monotweety

import android.content.Context
import com.codingfeline.twitter4kt.core.ConsumerKeys
import dagger.BindsInstance
import dagger.Component
import net.yslibrary.monotweety.data.SingletonDataModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SingletonDataModule::class,
        SingletonDataModule::class,
    ]
)
interface AppComponent {
    fun inject(app: App)

    fun userComponent(): UserComponent

    @Component.Factory
    interface Factory {
        fun create(
//            appModule: AppModule,
            @BindsInstance context: Context,
            @BindsInstance consumerKeys: ConsumerKeys,
        ): AppComponent
    }
}
