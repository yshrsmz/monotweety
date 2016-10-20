package net.yslibrary.monotweety

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * Created by yshrsmz on 2016/10/20.
 */
fun readJsonFromAssets(filename: String): String {
  val ASSET_BASE_PATH = "../app/src/test/assets/"

  val br = BufferedReader(InputStreamReader(FileInputStream("${ASSET_BASE_PATH}${filename}")))

  val sb = StringBuilder()
  var line = br.readLine()
  while (line != null) {
    sb.append(line)
    line = br.readLine()
  }

  br.close()
  return sb.toString()
}