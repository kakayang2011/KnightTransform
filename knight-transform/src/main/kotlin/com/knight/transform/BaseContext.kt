package com.knight.transform

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Project
import transform.task.WeavedClass
import java.io.File
import java.io.FileNotFoundException

open class BaseContext(val project: Project, val extension: BaseExtension) {

    val isLibrary = project.plugins.hasPlugin(LibraryPlugin::class.java)
    var weavedClassMap = ArrayList<WeavedClass>()

    open fun isWeaveableClass(filePath: String): Boolean {
        return filePath.endsWith(".class")
//                && !filePath.contains("R$")
//                && !filePath.contains("R.class")
//                && !filePath.contains("BuildConfig.class")
    }

//    private fun getSdkJarDir(): String {
//        val compileSdkVersion = android?.compileSdkVersion
//        return arrayOf(android?.sdkDirectory?.absolutePath, "platforms", compileSdkVersion).joinToString(File.separator)
//    }
//
//    fun buildDir(): File {
//        return File(project.buildDir, "")
//    }
//
//    @Throws(FileNotFoundException::class)
//    fun androidJar(): File {
//        val jar = File(getSdkJarDir(), "android.jar")
//        if (!jar.exists()) {
//            throw FileNotFoundException("Android jar not found!")
//        }
//        return jar
//    }
}