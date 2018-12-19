package com.knight.transform

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.android.builder.model.AndroidProject
import com.knight.transform.java.outmap.KnightConfig
import com.knight.transform.java.outmap.KnightConfigManager
import org.gradle.api.Plugin
import org.gradle.api.Project
import transform.task.OutPutMappingTask
import transform.task.WeavedClass
import java.util.concurrent.TimeUnit

class KnightPlugin : Plugin<Project> {
    val EXTENSION_NAME = "doubleCheckConfig"

    override fun apply(project: Project) {


        val android = project.extensions.getByType(AppExtension::class.java)
        val weavedVariantClassesMap = LinkedHashMap<String, List<WeavedClass>>()

        project.extensions.create("doubleCheckConfig", KnightConfig::class.java)

        android.applicationVariants.all {
            (it as ApplicationVariantImpl).let { variant ->
                println("===== variant name : ${variant.name}")
                val config = KnightConfigManager.knightConfig
                (project.extensions.getByName(EXTENSION_NAME) as KnightConfig).apply {
                    config.checkClassPath = checkClassPath
                    config.checkClassAnnotation = checkClassAnnotation
                    config.isScanJar = isScanJar
                }
                println("===== checkClassAnnotation name : ${config.checkClassAnnotation}")
                println("===== config name : ${config.checkClassPath}")
                createWriteMappingTask(project, variant, weavedVariantClassesMap)
            }
        }
        println("=====1 config name : ${KnightConfigManager.knightConfig.checkClassPath}")
        android.registerTransform(DoubleCheckTransform(weavedVariantClassesMap))
    }


    fun createWriteMappingTask(project: Project, variant: ApplicationVariantImpl
                               , weavedVariantClassesMap: LinkedHashMap<String, List<WeavedClass>>) {
        val mappingTaskName = "outputMappingFor${variant.name.capitalize()}"
        val doubleCheckTask = project.tasks.getByName("transformClassesWithDoubleCheckTransformFor${variant.name.capitalize()}")
        doubleCheckTask.apply {
            var startTime: Long = 0
            doFirst {
                startTime = System.nanoTime()
            }

            doLast {
                println("\n ${doubleCheckTask.name} -->COST: ${TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)} ms \n")
            }
        }
        val outputMappingTask = project.tasks.create(mappingTaskName, OutPutMappingTask::class.java) {
            (it as OutPutMappingTask).apply {
                classes.set(weavedVariantClassesMap)
                variantName.set(variant.name)
                outputMappingFile.set(com.android.utils.FileUtils.join(project.buildDir, AndroidProject.FD_OUTPUTS, "doubleCheck"
                        , "mapping", variant.name, "doubelMapping.txt"))
            }
        }

        outputMappingTask.apply {
            var startTime: Long = 0
            doFirst {
                startTime = System.nanoTime()
            }

            doLast {
                println("\n ${outputMappingTask.name} -->COST: ${TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)} ms \n")
            }
        }

        doubleCheckTask.finalizedBy(outputMappingTask)
        outputMappingTask.onlyIf { doubleCheckTask.didWork }
        outputMappingTask.dependsOn(doubleCheckTask)
    }

}