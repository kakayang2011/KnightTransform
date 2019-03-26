package com.knight.transform.config.extension

import com.knight.transform.BaseExtension

open class ConfigExtension(var packageName: String = "", var constantMap: HashMap<String, String> = HashMap()) : BaseExtension() {

}
