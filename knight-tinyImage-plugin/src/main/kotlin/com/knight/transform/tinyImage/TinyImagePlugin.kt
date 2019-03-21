package com.knight.transform.tinyImage

import com.android.build.gradle.TestedExtension
import com.android.build.gradle.api.BaseVariant
import com.knight.transform.BaseContext
import com.knight.transform.KnightTaskPlugin
import com.knight.transform.Utils.Log
import com.knight.transform.Utils.Timer
import com.knight.transform.tinyImage.extension.TinyImageExtension
import com.knight.transform.tinyImage.tasks.RevertTask
import com.knight.transform.tinyImage.tasks.TinyImageTask
import org.gradle.api.Project

class TinyImagePlugin : KnightTaskPlugin<TinyImageExtension, Context>() {

    private val EXTENSION_NAME = "tinyImage"
    private val TAG = "TinyImagePlugin";

    override fun createExtensions(): TinyImageExtension {
        project.extensions.create(EXTENSION_NAME, TinyImageExtension::class.java)
        return project.property(EXTENSION_NAME) as TinyImageExtension
    }


    override fun createTask(variant: BaseVariant, context: Context) {
        Log.isOpenLog = context.extension.log
        val mergeResourcesTask = project.tasks.findByName("merge${variant.name.capitalize()}Resources")
        val tinyTaskName = "TinyImage${variant.name.capitalize()}"

        val tinyPicTask = project.tasks.create(tinyTaskName, TinyImageTask::class.java).apply {
            this.variant = variant
            this.context = context
        }

        tinyPicTask.doFirst {
            Timer.start(it.name)
        }

        tinyPicTask.doLast {
            Timer.stop(it.name)
        }

        mergeResourcesTask?.let { task ->
            tinyPicTask.dependsOn(task.taskDependencies.getDependencies(task))
            tinyPicTask.finalizedBy(task)
            task.onlyIf { tinyPicTask.didWork }
            task.dependsOn(tinyPicTask)
        }

        val packageTask = project.tasks.findByName("package${variant.name.capitalize()}")
        val revertTaskName = "revert${variant.name.capitalize()}"
        val revertTask = project.tasks.create(revertTaskName, RevertTask::class.java).apply {
            this.context = context
        }

        revertTask.doFirst {
            Timer.start(it.name)
        }

        revertTask.doLast {
            Timer.stop(it.name)
        }

        packageTask?.run {
            packageTask.finalizedBy(revertTask)
            revertTask.onlyIf { packageTask.didWork }
            revertTask.dependsOn(packageTask)
        }
    }

    override fun getContext(project: Project, extension: TinyImageExtension, android: TestedExtension): Context {
        return Context(project, extension)
    }

}