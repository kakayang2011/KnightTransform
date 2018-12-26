package com.knight.transform.shrinkr

import com.android.build.api.transform.Transform
import com.android.build.gradle.TestedExtension
import com.knight.transform.KnightPlugin
import com.knight.transform.shrinkr.extension.ShrinkRExtension
import com.knight.transform.shrinkr.weave.AnalyzeRClassVisitor
import com.knight.transform.shrinkr.weave.ShrinkRClassVisitor
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class ShrinkRPlugin : KnightPlugin<ShrinkRExtension, Context>() {


    private val EXTENSION_NAME = "shrinkRConfig"
    override val isNeedPrintMapAndTaskCostTime: Boolean = true

    override fun createExtensions(): ShrinkRExtension {
        println("============1createExtensions")
        project.extensions.create(EXTENSION_NAME, ShrinkRExtension::class.java)
        (project.extensions.getByName(EXTENSION_NAME) as ShrinkRExtension).apply {
            println("============1scanJar: $isScanJar")
            context = Context(project, this)
            context.initWithWhiteList(keepList)
            return this
        }
    }

    override fun createTransform(): Transform {
        return ShrinkRTransform(context, this)
    }

    override fun getContext(project: Project, extension: ShrinkRExtension, android: TestedExtension): Context {
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
}