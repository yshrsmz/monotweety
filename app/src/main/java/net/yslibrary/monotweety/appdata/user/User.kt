package net.yslibrary.monotweety.appdata.user

data class User(
    val id: Long,
    val name: String,
    val screenName: String,
    val profileImageUrl: String,
    val _updatedAt: Long
)
