package net.yslibrary.monotweety.domain.twitterapp

import net.yslibrary.monotweety.data.twitterapp.AppInfo
import net.yslibrary.monotweety.data.twitterapp.TwitterAppRepository
import javax.inject.Inject

interface GetInstalledTwitterApps {
    suspend operator fun invoke(): List<AppInfo>
}

internal class GetInstalledTwitterAppsImpl @Inject constructor(
    private val repository: TwitterAppRepository,
) : GetInstalledTwitterApps {
    override suspend fun invoke(): List<AppInfo> {
        return repository.getInstalledApps()
    }
}
