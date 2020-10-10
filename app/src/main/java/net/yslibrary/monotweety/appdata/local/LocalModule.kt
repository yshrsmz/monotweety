package net.yslibrary.monotweety.appdata.local

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import com.pushtorefresh.storio3.sqlite.StorIOSQLite
import com.pushtorefresh.storio3.sqlite.impl.DefaultStorIOSQLite
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.appdata.user.User
import net.yslibrary.monotweety.appdata.user.local.resolver.UserSQLiteTypeMapping
import javax.inject.Singleton

@Module
class LocalModule {

    @Singleton
    @Provides
    fun provideDbOpenHelper(context: Context): SQLiteOpenHelper {
        return DbOpenHelper(context)
    }

    @Singleton
    @Provides
    fun provideStorIOSQLite(sqLiteOpenHelper: SQLiteOpenHelper): StorIOSQLite {
        return DefaultStorIOSQLite.builder()
            .sqliteOpenHelper(sqLiteOpenHelper)
            .addTypeMapping(User::class.java, UserSQLiteTypeMapping())
            .build()
    }
}
