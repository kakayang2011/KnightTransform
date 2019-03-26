package com.knight.transform.tinyImage

import com.android.build.gradle.TestedExtension
import com.android.build.gradle.api.BaseVariant
import com.android.tools.r8.utils.ZipUtils
import com.knight.transform.KnightTaskPlugin
import com.knight.transform.Utils.Log
import com.knight.transform.Utils.PrintAllTaskUtil
import com.knight.transform.Utils.Timer
import com.knight.transform.tinyImage.extension.TinyImageExtension
import com.knight.transform.tinyImage.tasks.RevertTask
import com.knight.transform.tinyImage.tasks.TinyImageTask
import com.knight.transform.tinyImage.utils.DownLoadUtil
import de.undercouch.gradle.tasks.download.Download
import org.gradle.api.Project
import java.io.File

class TinyImagePlugin : KnightTaskPlugin<TinyImageExtension, Context>() {

    private val EXTENSION_NAME = "tinyImage"

    override fun createExtensions(): TinyImageExtension {
        project.extensions.create(EXTENSION_NAME, TinyImageExtension::class.java)
        return project.property(EXTENSION_NAME) as TinyImageExtension
    }


    override fun createTask(variant: BaseVariant, context: Context) {
        Log.isOpenLog = context.extension.log
        if (context.extension.enablePrintTasks) {
            PrintAllTaskUtil.printAllTasks(context)
        }
        context.extension.rootPath = if (context.extension.rootPath.isEmpty()) project.rootDir.path else context.extension.rootPath
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

        if (!DownLoadUtil.tinyProgramExist(context)) {
            val downLoadTaskName = "downLoad${variant.name.capitalize()}"

            val downLoadTask = project.tasks.create(downLoadTaskName, Download::class.java)
            downLoadTask.src(DownLoadUtil.getDownUrl())
            downLoadTask.dest(context.extension.rootPath)


            tinyPicTask.let { task ->
                downLoadTask.dependsOn(task.taskDependencies.getDependencies(task))
                downLoadTask.finalizedBy(task)
                task.onlyIf { downLoadTask.didWork }
                task.dependsOn(downLoadTask)
            }

            downLoadTask.doLast { task ->
                task as Download
                task.outputFiles.forEach {
                    val file = File(context.extension.rootPath)
                    ZipUtils.unzip(it.name, file)
                    DownLoadUtil.chmodFile(file)
                    it.delete()
                }
            }
        }

        if (!context.extension.needRevert) {
            return
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