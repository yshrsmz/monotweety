package net.yslibrary.monotweety.data.user.local.resolver

import android.content.ContentValues
import com.pushtorefresh.storio3.sqlite.operations.put.DefaultPutResolver
import com.pushtorefresh.storio3.sqlite.queries.InsertQuery
import com.pushtorefresh.storio3.sqlite.queries.UpdateQuery
import net.yslibrary.monotweety.data.user.User
import net.yslibrary.monotweety.data.user.local.UserTable

class UserPutResolver : DefaultPutResolver<User>() {
    override fun mapToContentValues(entity: User): ContentValues {
        val cv = ContentValues(5)

        cv.put(UserTable.COLUMN_ID, entity.id)
        cv.put(UserTable.COLUMN_NAME, entity.name)
        cv.put(UserTable.COLUMN_SCREEN_NAME, entity.screenName)
        cv.put(UserTable.COLUMN_PROFILE_IMAGE_URL, entity.profileImageUrl)
        cv.put(UserTable.COLUMN__UPDATED_AT, entity._updatedAt)

        return cv
    }

    override fun mapToInsertQuery(entity: User): InsertQuery {
        return UserTable.insertQuery()
    }

    override fun mapToUpdateQuery(entity: User): UpdateQuery {
        return UserTable.updateById(entity.id)
    }
}
