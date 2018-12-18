package com.knight.plugin.doublecheck

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BasePlugin
import com.android.build.gradle.FeaturePlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.android.builder.model.AndroidProject
import com.android.utils.FileUtils
import com.knight.plugin.doublecheck.java.WeavedClass
import groovy.util.logging.Slf4j
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

import java.util.concurrent.TimeUnit

@Slf4j
class DoubleCheckPlugin implements Plugin<Project> {

    static final String EXTENSION_NAME = "doubleCheckConfig"

    @Override
    void apply(Project project) {
        println("=====this is double checkout plugin=====")

        def androidPlugin = [AppPlugin, LibraryPlugin, FeaturePlugin]
                .collect { project.plugins.findPlugin(it) as BasePlugin }
                .find { it != null }

        println("Found Plugin: {}, $androidPlugin")

        if (!androidPlugin) {
            throw new GradleException(
                    "'com.android.application' or 'com.android.library' or 'com.android.feature' plugin required.")
        }
        // AppExension就是build.gradle中android{...}一块
        def android = project.extensions.getByType(AppExtension)
        def weavedVariantClassesMap = new LinkedHashMap<String, List<WeavedClass>>()

        project.extensions.create(EXTENSION_NAME, DoubleCheckConfig)
        android.applicationVariants.all { ApplicationVariantImpl variant ->
            println("===== variant name : ${variant.name}")
            //拿到build.gradle中创建的Extension的值
            DoubleCheckConfig.checkClassPath = (project.extensions.getByName(EXTENSION_NAME) as DoubleCheckConfig).checkClassPath
            DoubleCheckConfig.checkClassAnnotation = (project.extensions.getByName(EXTENSION_NAME) as DoubleCheckConfig).checkClassAnnotation
            DoubleCheckConfig.isScanJar = (project.extensions.getByName(EXTENSION_NAME) as DoubleCheckConfig).isScanJar
            println("===== checkClassAnnotation name : ${DoubleCheckConfig.checkClassAnnotation}")
            println("===== config name : ${DoubleCheckConfig.checkClassPath}")
            createWriteMappingTask(project, variant, weavedVariantClassesMap)
        }
        println("=====1 config name : ${DoubleCheckConfig.checkClassPath}")
        // 注册自己的Transform
        def doubleCheckTransform = new DoubleCheckTransform(weavedVariantClassesMap)
        android.registerTransform(doubleCheckTransform)
    }

    static void createWriteMappingTask(Project project, BaseVariant variant,
                                       LinkedHashMap<String, List<WeavedClass>> weavedVariantClassesMap) {
        def mappingTaskName = "outputMappingFor${variant.name.capitalize()}"
        Task doubleCheckTask = project.tasks["transformClassesWithDoubleCheckTransformFor${variant.name.capitalize()}"]
        doubleCheckTask.configure {
            def startTime
            doFirst {
                startTime = System.nanoTime()
            }
            doLast {
                println()
                println " --> COST: ${TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)} ms"
                println()
            }
        }
        Task outputMappingTask = project.tasks.create(//
                name: "${mappingTaskName}",
                type: OutPutMappingTask) {

            classes = weavedVariantClassesMap

            variantName = variant.name
            outputMappingFile =
                    FileUtils.join(project.buildDir, AndroidProject.FD_OUTPUTS, 'doubleCheck', 'mapping',
                            variant.name, 'debouncedMapping.txt')
        }

        outputMappingTask.configure {
            def startTime
            doFirst {
                startTime = System.nanoTime()
            }
            doLast {
                println()
                println " --> COST: ${TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)} ms"
                println()
            }
        }

        doubleCheckTask.finalizedBy(outputMappingTask)

        outputMappingTask.onlyIf { doubleCheckTask.didWork }
        outputMappingTask.dependsOn(doubleCheckTask)

    }
}
