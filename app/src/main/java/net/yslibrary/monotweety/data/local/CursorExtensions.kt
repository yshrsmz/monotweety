package net.yslibrary.monotweety.data.local

import android.database.Cursor

/**
 * Created by yshrsmz on 2016/10/08.
 */
fun Cursor.getStringByName(name: String): String? = this.getString(this.getColumnIndex(name))

fun Cursor.getLongByName(name: String): Long = this.getLong(this.getColumnIndex(name))

fun Cursor.getIntByName(name: String): Int = this.getInt(this.getColumnIndex(name))