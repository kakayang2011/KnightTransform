package com.knight.transform.tinyImage.interceptor.impl

import com.android.tools.r8.utils.ZipUtils
import com.knight.transform.Utils.Timer
import com.knight.transform.tinyImage.interceptor.ITaskInterceptor
import com.knight.transform.tinyImage.interceptor.TaskChain
import com.knight.transform.tinyImage.utils.DownLoadUtil
import de.undercouch.gradle.tasks.download.Download
import java.io.File

class DownLoadTinyTaskInterceptor : ITaskInterceptor {
    override fun intercept(chain: TaskChain) {
        val context = chain.request().context
        val project = context.project
        val variant = context.variant
        val preTask = chain.request().task
        if (!DownLoadUtil.tinyProgramExist(context)) {
            val downLoadTaskName = "downLoad${variant.name.capitalize()}"

            val downLoadTask = project.tasks.create(downLoadTaskName, Download::class.java)
            downLoadTask.src(DownLoadUtil.getDownUrl())
            downLoadTask.dest(context.extension.rootPath)

            preTask?.let { task ->
                task.finalizedBy(downLoadTask)
                downLoadTask.onlyIf { task.didWork }
                downLoadTask.dependsOn(task)
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
            downLoadTask.doFirst {
                Timer.start(it.name)
            }

            downLoadTask.doLast {
                Timer.stop(it.name)
            }
            chain.request().task = downLoadTask
        }
        chain.process()
    }

}