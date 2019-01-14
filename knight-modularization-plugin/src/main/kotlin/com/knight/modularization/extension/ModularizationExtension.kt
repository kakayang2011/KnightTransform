package com.knight.modularization.extension

import com.knight.transform.BaseExtension

open class ModularizationExtension(var modulePath: String = "com/knight/doublecheck/library/DoubleCheckTool",
                                   var appPath: String = "com/knight/doublecheck/library/DoubleCheck",
                                   var serviceProvider: String = "",
                                   var serviceManagerPath: String = "",
                                   var serviceImplMapName: String = "",
                                   var serviceApplicationListName: String = ""
) : BaseExtension()

