package com.knight.transform.tinyImage.utils

import com.knight.transform.Utils.Log
import org.gradle.api.GradleException
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * Created by longlong on 2017/4/15.
 */
object CompressUtil {

    val TAG = "CompressUtil"
    fun compressImg(rootStr: String, imgFile: File) {
        if (ImageUtil.isImage(imgFile)) {
            val oldSize = imgFile.length()
            if (ImageUtil.isJPG(imgFile)) {
                CompressUtil.command(rootStr, "./guetzli ${imgFile.path} ${imgFile.path}")
            } else {
                CompressUtil.command(rootStr, "./pngquant --skip-if-larger --speed 3 --force --output ${imgFile.path} -- ${imgFile.path}")
            }
            val newSize = imgFile.length()
            Log.i(TAG, "oldsize: $oldSize  newsize: $newSize")
        }
    }

    fun command(rootStr: String, cmd: String) {
        val cdStr = run {
            val system = System.getProperty("os.name")
            when (system) {
                "Mac OS X" ->
                    "cd ${rootStr}mac/"
                "Linux" ->
                    "cd ${rootStr}linux/"
                "Windows" ->
                    "cd ${rootStr}windows/"
                else -> ""
            }
        }

        if (cdStr.isEmpty()) {
            Log.i(TAG, "McImage Not support this system")
            return
        }
        Log.i(TAG, "final command is : app/mctools/mac/$cmd")
        try {
            val process = Runtime.getRuntime().exec("app/mctools/mac/$cmd")
            process.waitFor()
        } catch (e: Exception) {
            Log.i(TAG, "exception is  : ${e.toString()}")
            throw  GradleException("$e you should download resource at http://www.baidu.com")
        }
    }

}
