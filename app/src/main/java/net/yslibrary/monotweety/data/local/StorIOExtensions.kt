package net.yslibrary.monotweety.data.local

import com.pushtorefresh.storio.sqlite.operations.delete.PreparedDelete
import com.pushtorefresh.storio.sqlite.operations.get.PreparedGet
import com.pushtorefresh.storio.sqlite.operations.put.PreparedPut

/**
 * Created by yshrsmz on 2016/10/08.
 */
fun <T> PreparedPut.Builder.withObject(entity: T) = `object`(entity)

fun <T> PreparedPut.Builder.withObjects(entities: Collection<T>) = objects(entities)

fun <T> PreparedGet.Builder.singleObject(entityClass: Class<T>) = `object`(entityClass)

fun <T> PreparedDelete.Builder.withObject(entity: T) = `object`(entity)

fun <T> PreparedDelete.Builder.withObjects(entities: Collection<T>) = objects(entities)