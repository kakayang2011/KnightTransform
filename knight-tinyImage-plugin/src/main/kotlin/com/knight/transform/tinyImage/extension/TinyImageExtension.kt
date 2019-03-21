package com.knight.transform.tinyImage.extension

import com.knight.transform.BaseExtension


open class TinyImageExtension(var log: Boolean = false,
                              var rootPath: String = "",
                              var compress: Boolean = true,
                              var webp: Boolean = false,
                              var needRevert: Boolean = true,
                              var whiteList: ArrayList<String> = ArrayList()) : BaseExtension() {
    var maxSize: Long = 1024 * 1024
}
