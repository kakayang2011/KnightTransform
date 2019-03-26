package com.knight.transform.tinyImage.utils

import com.knight.transform.Utils.Log
import org.gradle.api.GradleException
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object revertUtil {
    const val TAG = "revertUtil"
    fun revertFile(imageFile: File) {
        if (ImageUtil.isImage(imageFile)) {
            try {
                val process = Runtime.getRuntime().exec("git checkout ${imageFile.absolutePath}")
                process.waitFor()
//                val bufferReader = BufferedReader(InputStreamReader(process.inputStream))
//                Log.i( "revertFile is  : ${bufferReader.readLine()}")
            } catch (e: Exception) {
                Log.i( "exception is  : ${e}")
                throw  GradleException("$e you should 'git init' this program")
            }
        }
    }


    fun removeFile(imageFile: File) {
        try {
            val process = Runtime.getRuntime().exec("git clean -df ${imageFile.absolutePath}")
            process.waitFor()
//            val bufferReader = BufferedReader(InputStreamReader(process.inputStream))
//            Log.i( "removeFile is  : ${bufferReader.readLine()}")
        } catch (e: Exception) {
            Log.i( "exception is  : ${e}")
            throw  GradleException("$e you should 'git init' this program")
        }
    }
}