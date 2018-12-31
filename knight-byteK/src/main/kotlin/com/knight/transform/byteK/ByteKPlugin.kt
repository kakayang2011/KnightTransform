package com.knight.transform.byteK

import com.android.build.api.transform.Transform
import com.android.build.gradle.TestedExtension
import com.knight.transform.Interceptor.IClassVisitorInterceptor
import com.knight.transform.Interceptor.impl.BaseClassVisitorInterceptor
import com.knight.transform.KnightPlugin
import com.knight.transform.byteK.extension.ByteKExtension
import com.knight.transform.byteK.weave.shrinkR.AnalyzeRClassVisitor
import com.knight.transform.byteK.weave.doublecheck.DoubleCheckModifyClassVisitor
import com.knight.transform.byteK.weave.shrinkR.ShrinkRClassVisitor
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import java.util.ArrayList

class ByteKPlugin : KnightPlugin<ByteKExtension, Context>() {


    private val EXTENSION_NAME = "shrinkRConfig"
    override val isNeedPrintMapAndTaskCostTime: Boolean = true

    override fun createExtensions(): ByteKExtension {
        project.extensions.create(EXTENSION_NAME, ByteKExtension::class.java)
        (project.extensions.getByName(EXTENSION_NAME) as ByteKExtension).apply {
            context = Context(project, this)
            context.initWithWhiteList(keepList)
            return this
        }
    }


    override fun getContext(project: Project, extension: ByteKExtension, android: TestedExtension): Context {
        return Context(project, extension)
    }

    override fun createScanClassVisitor(classWriter: ClassWriter): ClassVisitor {
        return AnalyzeRClassVisitor(context, classWriter)
    }

    override fun createWeaveClassVisitor(classWriter: ClassWriter): ClassVisitor {
        return ShrinkRClassVisitor(context, classWriter)
    }

    override fun isNeedScanClass(): Boolean {
        return true
    }

    override fun isNeedScanWeaveRClass(): Boolean {
        return true
    }

    override fun getScanClassVisitorInterceptor(): List<IClassVisitorInterceptor>? {
        val list = ArrayList<IClassVisitorInterceptor>()
        list.add(BaseClassVisitorInterceptor {
            AnalyzeRClassVisitor(context, it)
        })
        return list
    }

    override fun getTransformName(): String {
        return "ByteKTransform"
    }

    override fun getWeaveClassVisitorInterceptor(): List<IClassVisitorInterceptor>? {
        val list = ArrayList<IClassVisitorInterceptor>()
        list.add(BaseClassVisitorInterceptor {
            ShrinkRClassVisitor(context, it)
        })
        list.add(BaseClassVisitorInterceptor {
            DoubleCheckModifyClassVisitor(context, it)
        })
        return list
    }
}