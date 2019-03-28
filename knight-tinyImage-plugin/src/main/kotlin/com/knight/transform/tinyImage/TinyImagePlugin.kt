package com.knight.transform.tinyImage

import com.android.build.gradle.TestedExtension
import com.android.build.gradle.api.BaseVariant
import com.knight.transform.KnightTaskPlugin
import com.knight.transform.Utils.Log
import com.knight.transform.Utils.PrintAllTaskUtil
import com.knight.transform.tinyImage.extension.TinyImageExtension
import com.knight.transform.tinyImage.interceptor.ITaskInterceptor
import com.knight.transform.tinyImage.interceptor.RealTaskInterceptorChain
import com.knight.transform.tinyImage.interceptor.TaskParams
import com.knight.transform.tinyImage.interceptor.impl.DownLoadTinyTaskInterceptor
import com.knight.transform.tinyImage.interceptor.impl.FindSamePicTaskInterceptor
import com.knight.transform.tinyImage.interceptor.impl.RevertTaskInterceptor
import com.knight.transform.tinyImage.interceptor.impl.TinyImageTaskInterceptor
import org.gradle.api.Project

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
        context.variant = variant
        context.extension.rootPath = if (context.extension.rootPath.isEmpty()) project.rootDir.path else context.extension.rootPath


        val interceptors = ArrayList<ITaskInterceptor>()
        interceptors.add(FindSamePicTaskInterceptor())
        interceptors.add(DownLoadTinyTaskInterceptor())
        interceptors.add(TinyImageTaskInterceptor())
        interceptors.add(RevertTaskInterceptor())
        val realTaskInterceptorChain = RealTaskInterceptorChain(0, interceptors, TaskParams(context))
        realTaskInterceptorChain.process()
    }

    override fun getContext(project: Project, extension: TinyImageExtension, android: TestedExtension): Context {
        return Context(project, extension)
    }

}