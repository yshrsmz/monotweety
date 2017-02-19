package net.yslibrary.monotweety

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

fun readJsonFromAssets(filename: String): String {
  val ASSET_BASE_PATH = "../app/src/test/assets/"

  val br = BufferedReader(InputStreamReader(FileInputStream("$ASSET_BASE_PATH$filename")))

  val sb = StringBuilder()
  var line = br.readLine()
  while (line != null) {
    sb.append(line)
    line = br.readLine()
  }

  br.close()
  return sb.toString()
}

fun newPackageInfo(appName: String, pacakgeName: String): PackageInfo {
  return PackageInfo().apply {
    packageName = pacakgeName
    applicationInfo = ApplicationInfo()

    applicationInfo.apply {
      packageName = pacakgeName
      name = appName
    }
  }
}