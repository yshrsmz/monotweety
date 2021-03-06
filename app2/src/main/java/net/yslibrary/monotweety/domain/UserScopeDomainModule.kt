package net.yslibrary.monotweety.domain

import dagger.Binds
import dagger.Module
import net.yslibrary.monotweety.domain.session.Logout
import net.yslibrary.monotweety.domain.session.LogoutImpl
import net.yslibrary.monotweety.domain.status.BuildAndValidateStatusString
import net.yslibrary.monotweety.domain.status.BuildAndValidateStatusStringImpl
import net.yslibrary.monotweety.domain.status.UpdateStatus
import net.yslibrary.monotweety.domain.status.UpdateStatusImpl
import net.yslibrary.monotweety.domain.user.FetchUser
import net.yslibrary.monotweety.domain.user.FetchUserImpl
import net.yslibrary.monotweety.domain.user.ObserveUser
import net.yslibrary.monotweety.domain.user.ObserveUserImpl

@Module
internal interface UserScopeDomainModule {
    @Binds
    fun bindUpdateStatus(impl: UpdateStatusImpl): UpdateStatus

    @Binds
    fun bindBuildAndValidateStatusString(impl: BuildAndValidateStatusStringImpl): BuildAndValidateStatusString

    @Binds
    fun bindFetchUser(impl: FetchUserImpl): FetchUser

    @Binds
    fun bindObserveUser(impl: ObserveUserImpl): ObserveUser

    @Binds
    fun bindLogout(impl: LogoutImpl): Logout
}
