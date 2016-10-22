package net.yslibrary.monotweety.data.license

import net.yslibrary.licenseadapter.GitHubLicenseEntry
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
          Licenses.noContent("Android SDK", "Google Inc.", "https://developer.android.com/sdk/terms.html"),
          Licenses.noContent("Fabric", "Twitter", "https://fabric.io/terms"),
          Licenses.fromGitHub("bluelinelabs/Conductor"),
          Licenses.fromGitHub("hdodenhof/CircleImageView"),
          Licenses.fromGitHub("gabrielemariotti/changeloglib", Licenses.LICENSE_APACHE_V2),
          Licenses.fromGitHub("google/dagger"),
          Licenses.fromGitHub("bumptech/glide", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHub("JakeWharton/RxBinding"),
          Licenses.fromGitHub("JakeWharton/timber"),
          Licenses.fromGitHub("f2prateek/rx-preferences"),
          Licenses.fromGitHub("pushtorefresh/storio"),
          GitHubLicenseEntry(Licenses.NAME_APACHE_V2, "ReactiveX/RxJava", "1.x", null, Licenses.FILE_NO_EXTENSION),
          GitHubLicenseEntry(Licenses.NAME_APACHE_V2, "ReactiveX/RxAndroid", "1.x", null, Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHub("sockeqwe/AdapterDelegates", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHub("twitter/twitter-text", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHub("twitter/twitter-kit-android", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHub("yshrsmz/LicenseAdapter", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHub("yshrsmz/RxEventBus", Licenses.FILE_NO_EXTENSION))

      Licenses.load(list)

      list
    }
  }
}