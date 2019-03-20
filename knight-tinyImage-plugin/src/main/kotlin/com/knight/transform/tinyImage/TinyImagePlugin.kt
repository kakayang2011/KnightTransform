package com.knight.transform.tinyImage

import com.android.build.gradle.TestedExtension
import com.android.build.gradle.api.BaseVariant
import com.knight.transform.BaseContext
import com.knight.transform.KnightPlugin
import com.knight.transform.Utils.Log
import com.knight.transform.tinyImage.extension.TinyImageExtension
import com.knight.transform.tinyImage.tasks.TinyImageTask
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class TinyImagePlugin : KnightPlugin<TinyImageExtension, Context>() {


    private val EXTENSION_NAME = "TinyImagePlugin"
    override val isNeedPrintMapAndTaskCostTime: Boolean = true

    override fun createExtensions(): TinyImageExtension {
        project.extensions.create(EXTENSION_NAME, TinyImageExtension::class.java)
        val tinyImageExtension = project.property(EXTENSION_NAME) as TinyImageExtension

        project.afterEvaluate {
            Log.i("liyachao===", "extions is ${tinyImageExtension.log}")
            Log.i("liyachao===", "extions isScanJar is ${tinyImageExtension.isScanJar}")
            Log.i("liyachao===", "extions dir is ${tinyImageExtension.executeFileDir}")

        }
        project.gradle.startParameter.taskNames.forEach {
            Log.i("liyachao===", "task name is ${it}")
        }
        return tinyImageExtension
    }


    override fun createTask(variant: BaseVariant, context: BaseContext<*>) {
//        super.createTask(variant, context)
        val mergeResourcesTask = project.tasks.findByName("merge${variant.name.capitalize()}Resources")
        val tinyTaskName = "TinyImage${variant.name.capitalize()}"
        Log.i("liyachao===", "tinyTaskName ${tinyTaskName}")
        Log.i("liyachao===", "mergeResourcesTask ${mergeResourcesTask}")

        val tinyPicTask = project.tasks.create(tinyTaskName, TinyImageTask::class.java).apply {
            this.variant = variant
            this.context = context as Context
        }

        mergeResourcesTask?.let { task ->
            task.finalizedBy(tinyPicTask)
            tinyPicTask.onlyIf { task.didWork }
            tinyPicTask.dependsOn(task)
        }

    }

    override fun getContext(project: Project, extension: TinyImageExtension, android: TestedExtension): Context {
        return Context(project, extension)
    }

    override fun createScanClassVisitor(classWriter: ClassWriter): ClassVisitor? {
        return null
    }

    override fun createWeaveClassVisitor(classWriter: ClassWriter): ClassVisitor? {
        return null
    }

    override fun isNeedWeaveClass(): Boolean {
        return false
    }

    override fun getTransformName(): String {
        return "TinyImageTransform"
    }

    override fun isNeedScanClass(): Boolean {
        return false
    }

    override fun isNeedScanWeaveRClass(): Boolean {
        return false
    }
}