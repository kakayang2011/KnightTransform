package com.knight.transform.config

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.knight.transform.config.extension.ConfigExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.lang.StringBuilder

class ConfigPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        //只在'applicatoin'中使用，否则抛出异常
        if (!project.plugins.hasPlugin(AppPlugin::class.java)) {
            throw GradleException("this plugin is not application")
        }
        //获取build.gradle中的"android"闭包
        val android = project.extensions.getByType(AppExtension::class.java)
        //创建自己的闭包
        val config = project.extensions.create("config", ConfigExtension::class.java)
        //遍历"android"闭包中的"buildTypes"闭包，一般有release和debug两种
        android.applicationVariants.all {
            it as ApplicationVariantImpl
            println("variant name: ${it.name}")

            //创建自己的config task
            val buildConfigTask = project.tasks.create("DemoBuildConfig${it.name.capitalize()}")
            //在task最后去创建java文件
            buildConfigTask.doLast { task ->
                createJavaConfig(it, config)
            }
            // 找到系统的buildConfig Task
            val generateBuildConfigTask = project.tasks.getByName(it.variantData.scope.taskContainer.generateBuildConfigTask?.name)
            //自己的Config Task 依赖于系统的Config Task
            generateBuildConfigTask.let {
                buildConfigTask.dependsOn(it)
                it.finalizedBy(buildConfigTask)
            }
        }
    }

    fun createJavaConfig(variant: ApplicationVariantImpl, config: ConfigExtension) {
        val FileName = "MyConfig"
        val constantStr = StringBuilder()
        constantStr.append("\n").append("package ")
                .append(config.packageName).append("; \n\n")
                .append("public class $FileName {").append("\n")
        config.constantMap.forEach {
            constantStr.append("public static final String ${it.key} = \"${it.value}\";\n")
        }
        constantStr.append("} \n")
        println("content: ${constantStr}")

        val outputDir = variant.variantData.scope.buildConfigSourceOutputDir
        val javaFile = File(outputDir, config.packageName.replace(".", "/") + "/$FileName.java")
        println("javaFilePath: ${javaFile.absolutePath}")
        javaFile.writeText(constantStr.toString(), Charsets.UTF_8)
    }

}