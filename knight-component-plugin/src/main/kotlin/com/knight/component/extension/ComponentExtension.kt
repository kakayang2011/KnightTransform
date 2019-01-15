package com.knight.modularization.extension

import com.knight.transform.BaseExtension

open class ModularizationExtension(var modulePath: String = "com/knight/modularization/library/ModuleSpec",
                                   var appPath: String = "com/knight/modularization/library/AppSpec",
                                   var serviceImpl: String = "com/knight/modularization/library/ServiceImpl",
                                   var serviceManagerPath: String = "com/knight/modularization/library/ServiceManager",
                                   var serviceImplMapName: String = "serviceImplMap",
                                   var serviceApplicationListName: String = "moduleApplications"
) : BaseExtension()

