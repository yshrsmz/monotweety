package net.yslibrary.monotweety.appdata.local

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import com.pushtorefresh.storio3.sqlite.StorIOSQLite
import com.pushtorefresh.storio3.sqlite.impl.DefaultStorIOSQLite
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.appdata.user.User
import net.yslibrary.monotweety.appdata.user.local.resolver.UserSQLiteTypeMapping
import net.yslibrary.monotweety.base.di.AppScope
import net.yslibrary.monotweety.base.di.Names
import javax.inject.Named

@Module
class LocalModule {

    @AppScope
    @Provides
    fun provideDbOpenHelper(@Named(Names.FOR_APP) context: Context): SQLiteOpenHelper {
        return DbOpenHelper(context)
    }

    @AppScope
    @Provides
    fun provideStorIOSQLite(sqLiteOpenHelper: SQLiteOpenHelper): StorIOSQLite {
        return DefaultStorIOSQLite.builder()
            .sqliteOpenHelper(sqLiteOpenHelper)
            .addTypeMapping(User::class.java, UserSQLiteTypeMapping())
            .build()
    }
}
