package com.knight.transform.tinyImage.utils

import com.knight.transform.Utils.Log
import com.knight.transform.tinyImage.Context
import java.io.File

object DownLoadUtil {

    fun tinyProgramExist(context: Context): Boolean {
        Log.i("checkFileExist")
        val rootStr = if (context.extension.rootPath.isNotEmpty()) context.extension.rootPath else context.project.rootDir.path
        val programs = arrayOf("pngquant", "guetzli", "cwebp")
        val programsWindow = arrayOf("pngquant.exe", "guetzli.exe", "cwebp.exe")
        val files = ArrayList<File>()
        val system = System.getProperty("os.name")
        when (system) {
            "Mac OS X" -> {
                programs.forEach {
                    files.add(File("${rootStr}/tinytools/mac/$it"))
                }
            }
            "Linux" -> {
                programs.forEach {
                    files.add(File("${rootStr}/tinytools/mac/$it"))
                }
            }
            "Windows" -> {
                programsWindow.forEach {
                    files.add(File("${rootStr}/tinytools/mac/$it"))
                }
            }
        }
        files.forEach {
            if (!it.exists()) {
                return false
            }
        }

        return true
    }

    fun getDownUrl(): String {
        val system = System.getProperty("os.name")
        return when (system) {
            "Mac OS X" -> {
                "https://github.com/296777513/KnightTransform/releases/download/1.0.2/tinytools_mac.zip"
            }
            "Linux" -> {
                "https://github.com/296777513/KnightTransform/releases/download/1.0.2/tinytools_linux.zip"
            }
            "Windows" -> {
                "https://github.com/296777513/KnightTransform/releases/download/1.0.2/tinytools_windows.zip"
            }
            else -> ""
        }
    }


    fun chmodFile(file: File) {
        val system = System.getProperty("os.name")
        if (system == "Windows") {
            return
        }
        if (file.isDirectory) {
            file.listFiles().forEach {
                if (it.isFile) {
                    val process = Runtime.getRuntime().exec("chmod +x ${it.absoluteFile}")
                    process.waitFor()
                } else {
                    chmodFile(it)
                }
            }
        }
    }
}