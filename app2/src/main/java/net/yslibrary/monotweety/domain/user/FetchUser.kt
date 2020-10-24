package net.yslibrary.monotweety.domain.user

interface FetchUser {
    suspend operator fun invoke()
}
