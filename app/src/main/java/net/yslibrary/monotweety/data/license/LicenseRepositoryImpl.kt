package net.yslibrary.monotweety.data.license

import net.yslibrary.licenseadapter.Library
import net.yslibrary.licenseadapter.Licenses
import net.yslibrary.monotweety.base.di.UserScope
import rx.Single
import java.util.*
import javax.inject.Inject

@UserScope
class LicenseRepositoryImpl @Inject constructor() : LicenseRepository {
  override fun get(): Single<List<Library>> {
    return Single.fromCallable {
      val list = listOf<Library>(
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
          Licenses.fromGitHubApacheV2("ReactiveX/RxJava", "1.x/${Licenses.FILE_NO_EXTENSION}"),
          Licenses.fromGitHubApacheV2("ReactiveX/RxAndroid", "1.x/${Licenses.FILE_NO_EXTENSION}"),
          Licenses.fromGitHubApacheV2("sockeqwe/AdapterDelegates", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHubApacheV2("twitter/twitter-text", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHubApacheV2("twitter/twitter-kit-android", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHubApacheV2("yshrsmz/LicenseAdapter", Licenses.FILE_NO_EXTENSION),
          Licenses.fromGitHubApacheV2("yshrsmz/RxEventBus", Licenses.FILE_NO_EXTENSION))

      // sort github hosted repos first
      list.sortedBy { it.getName().toLowerCase(Locale.ENGLISH) }
          .let {
            val mutable = it.toMutableList()
            mutable.addAll(0, listOf(
                Licenses.noContent("Android SDK", "Google Inc.", "https://developer.android.com/sdk/terms.html"),
                Licenses.noContent("Fabric", "Google Inc.", "https://fabric.io/terms")
            ))
            mutable.toList()
          }
    }
  }
}