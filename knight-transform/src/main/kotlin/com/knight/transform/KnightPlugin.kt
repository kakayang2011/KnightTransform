package com.knight.transform

import com.android.build.api.transform.Transform
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.android.builder.model.AndroidProject
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import transform.task.OutPutMappingTask
import transform.task.WeavedClass
import java.util.concurrent.TimeUnit

abstract class KnightPlugin : Plugin<Project> {
    protected abstract val isNeedPrintMapAndTaskCostTime: Boolean
    private lateinit var transform: Transform

    override fun apply(project: Project) {
        if (!project.plugins.hasPlugin(AppPlugin::class.java) && !project.plugins.hasPlugin(LibraryPlugin::class.java)) {
            throw  GradleException("'com.android.application' or 'com.android.library' plugin required!")
        }

        val aClass = if (project.plugins.hasPlugin(AppPlugin::class.java))
            AppExtension::class.java
        else LibraryExtension::class.java

        val android = project.extensions.getByType(aClass)

        val weavedVariantClassesMap = LinkedHashMap<String, List<WeavedClass>>()
        if (isNeedPrintMapAndTaskCostTime) {
            if (android is AppExtension) {
                android.applicationVariants.all {
                    createWriteMappingTask(project, it , weavedVariantClassesMap)
                }
            } else if (android is LibraryExtension) {
                android.libraryVariants.all {
                    createWriteMappingTask(project, it, weavedVariantClassesMap)
                }
            }
            createExtensions(project)
        }
        transform = createTransform(project, weavedVariantClassesMap)
        android.registerTransform(transform)
    }

    abstract fun createExtensions(project: Project)

    abstract fun createTransform(project: Project, weavedVariantClassesMap: LinkedHashMap<String, List<WeavedClass>>): Transform

    private fun createWriteMappingTask(project: Project, variant: BaseVariant
                                       , weavedVariantClassesMap: LinkedHashMap<String, List<WeavedClass>>) {
        val mappingTaskName = "outputMappingFor${variant.name.capitalize()}"
        val myTask = project.tasks.getByName("transformClassesWith${transform.name}For${variant.name.capitalize()}")
        myTask.apply {
            var startTime: Long = 0
            doFirst {
                startTime = System.nanoTime()
            }

            doLast {
                println("\n ${myTask.name} -->COST: ${TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)} ms \n")
            }
        }
        val outputMappingTask = project.tasks.create(mappingTaskName, OutPutMappingTask::class.java) {
            (it as OutPutMappingTask).apply {
                classes.set(weavedVariantClassesMap)
                variantName.set(variant.name)
                outputMappingFile.set(com.android.utils.FileUtils.join(project.buildDir, AndroidProject.FD_OUTPUTS, transform.name
                        , "mapping", variant.name, "${transform.name}Mapping.txt"))
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

        myTask.finalizedBy(outputMappingTask)
        outputMappingTask.onlyIf { myTask.didWork }
        outputMappingTask.dependsOn(myTask)
    }
}