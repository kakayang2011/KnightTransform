package com.knight.transform.tinyImage.tasks

import com.knight.transform.tinyImage.Context
import com.knight.transform.tinyImage.utils.RevertUtil
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class RevertTask : DefaultTask() {

    companion object {
        val TAG = "RevertTask"
    }

    lateinit var context: Context


    @TaskAction
    fun revertPicture() {

        context.compressPngList.forEach {
            RevertUtil.revertFile(it)
        }

        context.convertWebpList.forEach {
            RevertUtil.removeFile(it)
        }
    }


}