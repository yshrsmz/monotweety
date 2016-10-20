package net.yslibrary.monotweety.data.license

import net.yslibrary.licenseadapter.LicenseEntry
import net.yslibrary.licenseadapter.Licenses
import net.yslibrary.monotweety.base.di.UserScope
import rx.Single
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/10/10.
 */
@UserScope
class LicenseRepositoryImpl @Inject constructor() : LicenseRepository {
  override fun get(): Single<List<LicenseEntry>> {
    return Single.fromCallable {
      val list = listOf(
          Licenses.fromGitHub("bluelinelabs/conductor"),
          Licenses.fromGitHub("hdodenhof/CircleImageView"),
          Licenses.fromGitHub("gabrielemariotti/changeloglib", Licenses.LICENSE_APACHE_V2),
          Licenses.fromGitHub("google/dagger"),
          Licenses.fromGitHub("bumptech/glide", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHub("jakewharton/rxbinding"),
          Licenses.fromGitHub("jakewharton/timber"),
          Licenses.fromGitHub("f2prateek/rx-preferences"),
          Licenses.fromGitHub("pushtorefresh/storio"),
          Licenses.fromGitHub("ReactiveX/RxJava", "1.x/${Licenses.FILE_NO_EXTENSION}"),
          Licenses.fromGitHub("ReactiveX/RxAndroid", "1.x/${Licenses.FILE_NO_EXTENSION}"),
          Licenses.fromGitHub("sockeqwe/AdapterDelegates", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHub("twitter/twitter-text", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHub("twitter/twitter-kit-android", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHub("yshrsmz/licenseadapter", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHub("yshrsmz/rxeventbus", Licenses.FILE_NO_EXTENSION))

      Licenses.load(list)

      list
    }
  }
}