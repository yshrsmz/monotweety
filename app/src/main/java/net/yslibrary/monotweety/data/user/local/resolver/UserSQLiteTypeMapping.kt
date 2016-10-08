package net.yslibrary.monotweety.data.user.local.resolver

import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping
import net.yslibrary.monotweety.data.user.User

/**
 * Created by yshrsmz on 2016/10/08.
 */
class UserSQLiteTypeMapping : SQLiteTypeMapping<User>(
    UserPutResolver(),
    UserGetResolver(),
    UserDeleteResolver())