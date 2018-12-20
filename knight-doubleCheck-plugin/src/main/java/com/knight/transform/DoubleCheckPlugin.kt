package com.knight.transform

import com.android.build.api.transform.Transform
import com.android.build.gradle.AppExtension
import com.knight.transform.extension.KnightConfig
import com.knight.transform.extension.KnightConfigManager
import org.gradle.api.Project
import transform.task.WeavedClass

class DoubleCheckPlugin() : KnightPlugin() {
    private val EXTENSION_NAME = "doubleCheckConfig"
    override val isNeedPrintMapAndTaskCostTime: Boolean = true


    override fun createExtensions(project: Project) {
        println("============1createExtensions")
        project.extensions.create("doubleCheckConfig", KnightConfig::class.java)
        val config = KnightConfigManager.knightConfig
        (project.extensions.getByName(EXTENSION_NAME) as KnightConfig).apply {
            config.checkClassPath = checkClassPath
            config.checkClassAnnotation = checkClassAnnotation
            config.scanJar = scanJar
            println("============1scanJar: $scanJar")
        }
    }

    override fun createTransform(project: Project, weavedVariantClassesMap: LinkedHashMap<String, List<WeavedClass>>): Transform {
        println("============createTransform")
        return DoubleCheckTransform(weavedVariantClassesMap, project)
    }
}