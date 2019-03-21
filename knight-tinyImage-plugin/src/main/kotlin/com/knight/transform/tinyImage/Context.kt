package com.knight.transform.tinyImage

import com.knight.transform.BaseContext
import com.knight.transform.tinyImage.extension.TinyImageExtension
import org.gradle.api.Project
import java.io.File

class Context(project: Project,
              extension: TinyImageExtension) : BaseContext<TinyImageExtension>(project, extension) {
    companion object {
        const val JPG = ".jpg"
        const val JPEG = ".jpeg"
        const val PNG = ".png"
        const val DOT_9PNG = ".9.png"

    }

    val compressPngList = ArrayList<File>()
    val convertWebpList = ArrayList<File>()


}