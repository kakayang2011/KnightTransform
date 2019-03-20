package com.knight.transform.tinyImage.utils

import com.knight.transform.tinyImage.Context
import java.io.File

object ImageUtil {
    fun isImage(file: File): Boolean {
        return (file.name.endsWith(Context.JPG) ||
                file.name.endsWith(Context.PNG) ||
                file.name.endsWith(Context.JPEG)
                ) && !file.name.endsWith(Context.DOT_9PNG)
    }

    fun isJPG(file: File): Boolean {
        return file.name.endsWith(Context.JPG) || file.name.endsWith(Context.JPEG)
    }
}