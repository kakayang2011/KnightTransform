package com.knight.transform.config

import com.knight.transform.BaseContext
import com.knight.transform.config.extension.ConfigExtension
import org.gradle.api.Project

class Context(project: Project,
              extension: ConfigExtension) : BaseContext(project, extension) {

}