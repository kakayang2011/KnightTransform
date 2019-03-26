package com.knight.transform.shrinkinline

import com.android.build.gradle.TestedExtension
import com.knight.transform.KnightPlugin
import com.knight.transform.Utils.Log
import com.knight.transform.shrinkinline.extension.ShrinkInlineExtension
import com.knight.transform.shrinkinline.weave.AnalyzeInlineClassVisitor
import com.knight.transform.shrinkinline.weave.ShrinkInlineClassVisitor
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class ShrinkInlinePlugin : KnightPlugin<ShrinkInlineExtension, Context>() {


    private val EXTENSION_NAME = "shrinkInlineConfig"
    override val isNeedPrintMapAndTaskCostTime: Boolean = true

    override fun createExtensions(): ShrinkInlineExtension {
        project.extensions.create(EXTENSION_NAME, ShrinkInlineExtension::class.java)
        (project.extensions.getByName(EXTENSION_NAME) as ShrinkInlineExtension).apply {
            project.afterEvaluate {
                context = Context(it, this)
                Log.isOpenLog = this.log
                Log.setLogLevel(this.logLevel)
            }
            return this
        }
    }


    override fun getContext(project: Project, extension: ShrinkInlineExtension, android: TestedExtension): Context {
        return Context(project, extension)
    }

    override fun createScanClassVisitor(classWriter: ClassWriter): ClassVisitor {
        return AnalyzeInlineClassVisitor(context, classWriter)
    }

    override fun createWeaveClassVisitor(classWriter: ClassWriter): ClassVisitor {
        return ShrinkInlineClassVisitor(context, classWriter)
    }

    override fun getTransformName(): String {
        return "ShrinkInlineTransform"
    }

    override fun isNeedScanClass(): Boolean {
        return true
    }

    override fun isNeedScanWeaveRClass(): Boolean {
        return true
    }
}