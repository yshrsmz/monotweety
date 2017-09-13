package net.yslibrary.monotweety.data.appinfo

enum class TwitterApp(val packageName: String) {
  BEETER("me.b0ne.android.apps.beeter"),
  BIYONTTER("jp.gifu.abs104a.twitterproject"),
  ECHOFON("com.echofon"),
  FALCON_PRO_3("com.jv.materialfalcon"),
  FEATHER("com.covelline.feather"),
  FENIX("it.mvilla.android.fenix"),
  FENIX2("it.mvilla.android.fenix2"),
  FLAMINGO("com.samruston.twitter"),
  HOOTSUITE("com.hootsuite.droid.full"),
  JANETTER_FREE("net.janesoft.janetter.android.free"),
  JANETTER_PRO("net.janesoft.janetter.android.pro"),
  JIGTWI("jp.jig.jigtwi.android"),
  JUSTAWAY("info.justaway"),
  MATECHA("net.wakamesoba98.matecha"),
  PLUME("com.levelup.touiteur"),
  ROBIRD("com.aaplab.robird"),
  SOBACHA("net.wakamesoba98.sobacha"),
  SURUYATU3("com.suruyatu3"),
  SURUYATU3_PRO("com.suruyatu3.pro"),
  TALON("com.klinker.android.twitter_l"),
  TWEECHA("net.sinproject.android.tweecha"),
  TWEECHA_PRIME("net.sinproject.android.tweecha.prime"),
  TWEETCASTER("com.handmark.tweetcaster"),
  TWEETCASTER_PRO("com.handmark.tweetcaster.premium"),
  TWEETINGS("com.dwdesign.tweetings"),
  TWICCA("jp.r246.twicca"),
  TWIDERE("org.mariotaku.twidere"),
  TWIPPLE("jp.ne.biglobe.twipple"),
  TWITAMA("com.softama.twitama"),
  TWITAMA_PLUS("com.softama.twitamaplus"),
  TWITCLE_PLUS("jp.yoshika.twitcle.plus"),
  TWITPANE("com.twitpane"),
  TWITPANE_PLUS("com.twitpane.premium"),
  TWITPANE_CLASSIC("com.twitpane.classic"),
  TWITTER("com.twitter.android"),
  TWITTNUKER("de.vanita5.twittnuker"),
  TWITWICK("jp.takmurata.twitwick2"),
  TWLTRUS("net.yuzumone.twltrus"),
  YUKARI("shibafu.yukari"),

  NONE("");

  companion object {
    fun packages(): List<String> {
      return values().map { it.packageName }
    }

    fun fromPackageName(packageName: String): TwitterApp {
      return values().filter { it.packageName == packageName }
          .firstOrNull() ?: NONE
    }
  }
}