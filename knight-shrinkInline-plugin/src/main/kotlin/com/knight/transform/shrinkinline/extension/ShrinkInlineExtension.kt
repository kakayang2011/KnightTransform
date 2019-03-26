package com.knight.transform.shrinkinline.extension

import com.knight.transform.BaseExtension
import com.knight.transform.Utils.Log

open class ShrinkInlineExtension(var log: Boolean = false) : BaseExtension() {
    var logLevel = Log.LogLevel.INFO.value
}
