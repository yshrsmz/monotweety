package net.yslibrary.monotweety.data.user

data class User(
    val id: String,
    val name: String,
    val screenName: String,
    val profileImageUrl: String,
    val updatedAt: Long
)
