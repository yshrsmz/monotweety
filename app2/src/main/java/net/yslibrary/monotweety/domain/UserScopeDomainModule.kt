package net.yslibrary.monotweety.domain

import dagger.Binds
import dagger.Module
import net.yslibrary.monotweety.domain.status.UpdateStatus
import net.yslibrary.monotweety.domain.status.UpdateStatusImpl

@Module
internal interface UserScopeDomainModule {
    @Binds
    fun bindUpdateStatus(impl: UpdateStatusImpl): UpdateStatus
}
