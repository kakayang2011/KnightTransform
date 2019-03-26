package com.knight.component.extension

import com.knight.transform.BaseExtension

open class ComponentExtension(var modulePath: String = "com/knight/component/library/ModuleSpec",
                              var appPath: String = "com/knight/component/library/AppSpec",
                              var serviceImpl: String = "com/knight/component/library/ServiceImpl",
                              var serviceManagerPath: String = "com/knight/component/library/ServiceManager",
                              var serviceImplMapName: String = "serviceImplMap",
                              var serviceApplicationListName: String = "moduleApplications"
) : BaseExtension()

