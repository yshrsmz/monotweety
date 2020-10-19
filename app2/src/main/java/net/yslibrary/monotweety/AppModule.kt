package net.yslibrary.monotweety

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import net.yslibrary.monotweety.base.CoroutineDispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
interface AppModule {

    companion object {
        @Provides
        fun provideFirebaseAnalytics(context: Context): FirebaseAnalytics {
            return FirebaseAnalytics.getInstance(context)
        }

        @Singleton
        @Provides
        fun provideCoroutineDispatchers(): CoroutineDispatchers {
            return newCoroutineDispatchers()
        }
    }
}

private fun newCoroutineDispatchers(): CoroutineDispatchers {
    return object : CoroutineDispatchers {
        override val main: CoroutineContext = Dispatchers.Main
        override val background: CoroutineContext = Dispatchers.Default
    }
}
