package com.knight.transform.tinyImage.interceptor.impl

import com.knight.transform.Utils.Timer
import com.knight.transform.tinyImage.interceptor.ITaskInterceptor
import com.knight.transform.tinyImage.interceptor.TaskChain
import com.knight.transform.tinyImage.tasks.FindSamePicTask
import org.gradle.api.DefaultTask
import org.gradle.api.Task

class FindSamePicTaskInterceptor : ITaskInterceptor {

    override fun intercept(chain: TaskChain) {
        val project = (chain.request()).context.project
        val variant = chain.request().context.variant

        val mergeResourcesTask = project.tasks.findByName("merge${variant.name.capitalize()}Resources")

        val findSamePicTaskName = "findSamePic${variant.name.capitalize()}"

        val findSamePicTask = project.tasks.create(findSamePicTaskName, FindSamePicTask::class.java).apply {
            this.variant = variant
            this.context = chain.request().context
        }

        mergeResourcesTask?.let { task ->
            findSamePicTask.dependsOn(task.taskDependencies.getDependencies(task))
            findSamePicTask.finalizedBy(task)
            task.onlyIf { findSamePicTask.didWork }
            task.dependsOn(findSamePicTask)
        }

        findSamePicTask.doFirst {
            Timer.start(it.name)
        }

        findSamePicTask.doLast {
            Timer.stop(it.name)
        }
        chain.request().task = findSamePicTask
        chain.process()
    }


}