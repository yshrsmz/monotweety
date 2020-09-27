package net.yslibrary.monotweety.appdata.user.local

import com.pushtorefresh.storio3.sqlite.queries.DeleteQuery
import com.pushtorefresh.storio3.sqlite.queries.InsertQuery
import com.pushtorefresh.storio3.sqlite.queries.Query
import com.pushtorefresh.storio3.sqlite.queries.UpdateQuery

class UserTable {

    companion object {
        const val TABLE = "user"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_SCREEN_NAME = "screen_name"
        const val COLUMN_PROFILE_IMAGE_URL = "profile_image_url"
        const val COLUMN__UPDATED_AT = "_updated_at"

        const val CREATE_TABLE =
            "CREATE TABLE $TABLE(" +
                "$COLUMN_ID INTEGER NOT NULL PRIMARY KEY, " +
                "$COLUMN_NAME TEXT NOT NULL, " +
                "$COLUMN_SCREEN_NAME TEXT NOT NULL, " +
                "$COLUMN_PROFILE_IMAGE_URL TEXT NOT NULL, " +
                "$COLUMN__UPDATED_AT INTEGER NOT NULL" +
                ");"

        fun queryById(id: Long) = Query.builder()
            .table(TABLE)
            .where("$COLUMN_ID = ?")
            .whereArgs(id)
            .build()

        fun deleteById(id: Long) = DeleteQuery.builder()
            .table(TABLE)
            .where("$COLUMN_ID = ?")
            .whereArgs(id)
            .build()

        fun insertQuery() = InsertQuery.builder()
            .table(TABLE)
            .build()

        fun updateById(id: Long) = UpdateQuery.builder()
            .table(TABLE)
            .where("$COLUMN_ID = ?")
            .whereArgs(id)
            .build()
    }
}
