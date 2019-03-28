package com.knight.transform.tinyImage.interceptor.impl

import com.android.tools.r8.utils.ZipUtils
import com.knight.transform.Utils.Timer
import com.knight.transform.tinyImage.interceptor.ITaskInterceptor
import com.knight.transform.tinyImage.interceptor.TaskChain
import com.knight.transform.tinyImage.tasks.TinyImageTask
import com.knight.transform.tinyImage.utils.DownLoadUtil
import de.undercouch.gradle.tasks.download.Download
import java.io.File

class TinyImageTaskInterceptor : ITaskInterceptor {
    override fun intercept(chain: TaskChain) {
        val context = chain.request().context
        val project = context.project
        val variant = context.variant
        val preTask = chain.request().task

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
        preTask?.let { task ->
            task.finalizedBy(tinyPicTask)
            tinyPicTask.onlyIf { task.didWork }
            tinyPicTask.dependsOn(task)
        }
        chain.request().task = tinyPicTask
        chain.process()
    }

}