package com.knight.transform.tinyImage.tasks

import com.knight.transform.Utils.Log
import com.knight.transform.tinyImage.Context
import com.knight.transform.tinyImage.utils.revertUtil
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class RevertTask : DefaultTask() {

    companion object {
        val TAG = "RevertTask"
    }

    lateinit var context: Context


    @TaskAction
    fun revertPicture() {
//        Log.i(TAG, "revertPicture run")

        context.compressPngList.forEach {
            //            Log.i(TAG, "compressPngFile ${it.name}")
            revertUtil.revertFile(it)
        }

        context.convertWebpList.forEach {
            //            Log.i(TAG, "convertWebpList ${it.name}")
            revertUtil.removeFile(it)
        }
    }


}