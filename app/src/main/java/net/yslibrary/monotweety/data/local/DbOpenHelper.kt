package net.yslibrary.monotweety.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import net.yslibrary.monotweety.data.user.local.UserTable

/**
 * Created by yshrsmz on 2016/10/08.
 */
class DbOpenHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

  override fun onCreate(db: SQLiteDatabase) {
    db.execSQL(UserTable.CREATE_TABLE)
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

  }

  companion object {
    const val DB_NAME = "monotweety_db"
    const val DB_VERSION = 1
  }
}