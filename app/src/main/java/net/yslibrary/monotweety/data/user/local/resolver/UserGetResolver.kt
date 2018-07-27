package net.yslibrary.monotweety.data.user.local.resolver

import android.database.Cursor
import com.pushtorefresh.storio3.sqlite.StorIOSQLite
import com.pushtorefresh.storio3.sqlite.operations.get.DefaultGetResolver
import net.yslibrary.monotweety.data.local.getLongByName
import net.yslibrary.monotweety.data.local.getStringByName
import net.yslibrary.monotweety.data.user.User
import net.yslibrary.monotweety.data.user.local.UserTable

class UserGetResolver : DefaultGetResolver<User>() {
    override fun mapFromCursor(storIOSQLite: StorIOSQLite, cursor: Cursor): User {
        return User(
            id = cursor.getLongByName(UserTable.COLUMN_ID),
            name = cursor.getStringByName(UserTable.COLUMN_NAME)!!,
            screenName = cursor.getStringByName(UserTable.COLUMN_SCREEN_NAME)!!,
            profileImageUrl = cursor.getStringByName(UserTable.COLUMN_PROFILE_IMAGE_URL)!!,
            _updatedAt = cursor.getLongByName(UserTable.COLUMN__UPDATED_AT))
    }
}
