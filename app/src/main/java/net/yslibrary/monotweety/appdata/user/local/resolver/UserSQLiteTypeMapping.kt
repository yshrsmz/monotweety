package net.yslibrary.monotweety.appdata.user.local.resolver

import com.pushtorefresh.storio3.sqlite.SQLiteTypeMapping
import net.yslibrary.monotweety.appdata.user.User

class UserSQLiteTypeMapping : SQLiteTypeMapping<User>(
    UserPutResolver(),
    UserGetResolver(),
    UserDeleteResolver()
)
