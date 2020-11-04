package net.yslibrary.monotweety.domain.setting

import net.yslibrary.monotweety.data.twitterapp.AppInfo
import net.yslibrary.monotweety.data.twitterapp.TwitterApp
import net.yslibrary.monotweety.data.twitterapp.TwitterAppRepository
import javax.inject.Inject

interface GetTwitterAppByPackageName {
    suspend operator fun invoke(packageName: String): AppInfo?
}

internal class GetTwitterAppByPackageNameImpl @Inject constructor(
    private val twitterAppRepository: TwitterAppRepository,
) : GetTwitterAppByPackageName {
    override suspend fun invoke(packageName: String): AppInfo? {
        val twitterApp = TwitterApp.fromPackageName(packageName)
        if (twitterApp == TwitterApp.NONE) return null
        return twitterAppRepository.getByPackageName(twitterApp)
    }
}
