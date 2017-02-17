package net.yslibrary.monotweety.data.user

data class User(val id: Long,
                val name: String,
                val screenName: String,
                val profileImageUrl: String,
                val _updatedAt: Long)