package net.yslibrary.monotweety.data.user.local.resolver

import com.pushtorefresh.storio3.sqlite.operations.delete.DefaultDeleteResolver
import com.pushtorefresh.storio3.sqlite.queries.DeleteQuery
import net.yslibrary.monotweety.data.user.User
import net.yslibrary.monotweety.data.user.local.UserTable

class UserDeleteResolver : DefaultDeleteResolver<User>() {
    override fun mapToDeleteQuery(entity: User): DeleteQuery {
        return UserTable.deleteById(entity.id)
    }
}
