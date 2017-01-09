package net.yslibrary.monotweety.data.license

import net.yslibrary.licenseadapter.GitHubLicenseEntry
import net.yslibrary.licenseadapter.LicenseEntry
import net.yslibrary.licenseadapter.Licenses
import net.yslibrary.licenseadapter.internal.BaseLicenseEntry
import net.yslibrary.monotweety.base.di.UserScope
import rx.Single
import java.util.*
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/10/10.
 */
@UserScope
class LicenseRepositoryImpl @Inject constructor() : LicenseRepository {
  override fun get(): Single<List<LicenseEntry>> {
    return Single.fromCallable {
      val list = listOf<BaseLicenseEntry>(
          Licenses.fromGitHubApacheV2("bluelinelabs/Conductor"),
          Licenses.fromGitHubApacheV2("hdodenhof/CircleImageView"),
          Licenses.fromGitHub("gabrielemariotti/changeloglib", Licenses.LICENSE_APACHE_V2),
          Licenses.fromGitHubApacheV2("google/dagger"),
          Licenses.fromGitHubApacheV2("bumptech/glide", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHubApacheV2("JakeWharton/RxBinding"),
          Licenses.fromGitHubApacheV2("JakeWharton/timber"),
          Licenses.fromGitHubApacheV2("JakeWharton/ThreeTenABP", Licenses.FILE_TXT),
          Licenses.fromGitHubApacheV2("f2prateek/rx-preferences"),
          Licenses.fromGitHubApacheV2("pushtorefresh/storio"),
          GitHubLicenseEntry(Licenses.NAME_APACHE_V2, "ReactiveX/RxJava", "1.x", null, Licenses.FILE_NO_EXTENSION),
          GitHubLicenseEntry(Licenses.NAME_APACHE_V2, "ReactiveX/RxAndroid", "1.x", null, Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHubApacheV2("sockeqwe/AdapterDelegates", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHubApacheV2("twitter/twitter-text", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHubApacheV2("twitter/twitter-kit-android", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHubApacheV2("yshrsmz/LicenseAdapter", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHubApacheV2("yshrsmz/RxEventBus", Licenses.FILE_NO_EXTENSION))

      // sort github hosted repos first
      list.sortedBy { it.name.toLowerCase(Locale.ENGLISH) }
          .let {
            val mutable = it.toMutableList()
            mutable.addAll(0, listOf(
                Licenses.noContent("Android SDK", "Google Inc.", "https://developer.android.com/sdk/terms.html"),
                Licenses.noContent("Fabric", "Twitter", "https://fabric.io/terms")
            ))
            mutable.toList()
          }
          .apply { Licenses.load(this) }
    }
  }
}