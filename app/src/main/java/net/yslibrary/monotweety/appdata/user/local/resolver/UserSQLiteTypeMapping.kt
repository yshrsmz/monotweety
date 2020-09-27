package net.yslibrary.monotweety.data.user.local.resolver

import com.pushtorefresh.storio3.sqlite.SQLiteTypeMapping
import net.yslibrary.monotweety.data.user.User

class UserSQLiteTypeMapping : SQLiteTypeMapping<User>(
    UserPutResolver(),
    UserGetResolver(),
    UserDeleteResolver()
)
