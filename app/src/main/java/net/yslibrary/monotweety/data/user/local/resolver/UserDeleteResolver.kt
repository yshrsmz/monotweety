package net.yslibrary.monotweety.data.user.local.resolver

import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery
import net.yslibrary.monotweety.data.user.User
import net.yslibrary.monotweety.data.user.local.UserTable

/**
 * Created by yshrsmz on 2016/10/08.
 */
class UserDeleteResolver : DefaultDeleteResolver<User>() {
  override fun mapToDeleteQuery(entity: User): DeleteQuery {
    return UserTable.deleteById(entity.id)
  }
}