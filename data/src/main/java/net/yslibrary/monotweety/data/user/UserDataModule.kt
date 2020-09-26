package net.yslibrary.monotweety.data.user

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import net.yslibrary.monotweety.data.user.local.UserPreferencesSerializer

object UserDataModule {

    fun provideUserDataStore(context: Context): DataStore<UserPreferences> {
        return context.createDataStore(
            fileName = "user_prefs.pb",
            serializer = UserPreferencesSerializer
        )
    }
}