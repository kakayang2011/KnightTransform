package com.knight.transform.tinyImage.extension

import com.knight.transform.BaseExtension


open class TinyImageExtension(var log: Boolean = false,
                              var rootPath: String = "",
                              var compress: Boolean = true,
                              var webp: Boolean = false) : BaseExtension()
