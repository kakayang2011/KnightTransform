package com.knight.transform.tinyImage.utils

import com.knight.transform.Utils.Log
import org.gradle.api.GradleException
import java.io.File

object revertUtil {
    const val TAG = "revertUtil"
    fun revertFile(imageFile: File) {
        if (ImageUtil.isImage(imageFile)) {
            try {
                val process = Runtime.getRuntime().exec("git checkout ${imageFile.absolutePath}")
                process.waitFor()
            } catch (e: Exception) {
                Log.i(TAG, "exception is  : ${e}")
                throw  GradleException("$e you should 'git init' this program")
            }
        }
    }


    fun removeFile(imageFile: File) {
        if (ImageUtil.isImage(imageFile)) {
            try {
                val process = Runtime.getRuntime().exec("git clean -df ${imageFile.absolutePath}")
                process.waitFor()
            } catch (e: Exception) {
                Log.i(TAG, "exception is  : ${e}")
                throw  GradleException("$e you should 'git init' this program")
            }
        }
    }
}