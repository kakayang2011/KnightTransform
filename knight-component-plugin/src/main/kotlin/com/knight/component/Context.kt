package com.knight.component

import com.android.build.gradle.AppExtension
import com.knight.transform.BaseContext
import com.knight.transform.BaseExtension
import org.gradle.api.Project
import org.gradle.internal.impldep.com.google.api.client.util.ArrayMap

class Context(project: Project,
              extension: BaseExtension) : BaseContext(project, extension) {

    val moduleApplications = ArrayList<String>()

    val serviceImpl = HashMap<String, String>()
}