package com.knight.transform.tinyImage.interceptor.impl

import com.android.tools.r8.utils.ZipUtils
import com.knight.transform.Utils.Timer
import com.knight.transform.tinyImage.interceptor.ITaskInterceptor
import com.knight.transform.tinyImage.interceptor.TaskChain
import com.knight.transform.tinyImage.tasks.RevertTask
import com.knight.transform.tinyImage.tasks.TinyImageTask
import com.knight.transform.tinyImage.utils.DownLoadUtil
import de.undercouch.gradle.tasks.download.Download
import java.io.File

class RevertTaskInterceptor : ITaskInterceptor {
    override fun intercept(chain: TaskChain) {
        val context = chain.request().context
        val project = context.project
        val variant = context.variant

        if (context.extension.needRevert) {
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
        chain.process()
    }

}