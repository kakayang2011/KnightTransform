package com.knight.transform.doubleCheck

import com.android.build.gradle.TestedExtension
import com.knight.transform.doubleCheck.extension.DoubleCheckExtension
import com.knight.transform.doubleCheck.weave.DoubleCheckModifyClassAdapter
import com.knight.transform.KnightPlugin
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class DoubleCheckPlugin : KnightPlugin<DoubleCheckExtension, Context>() {


    private val EXTENSION_NAME = "doubleCheck"
    override val isNeedPrintMapAndTaskCostTime: Boolean = true

    override fun createExtensions(): DoubleCheckExtension {
        project.extensions.create(EXTENSION_NAME, DoubleCheckExtension::class.java)
        (project.extensions.getByName(EXTENSION_NAME) as DoubleCheckExtension).apply {
            context = Context(project, this)
            return this
        }
    }

    override fun createScanClassVisitor(classWriter: ClassWriter): ClassVisitor? {
        return null
    }

    override fun getTransformName(): String {
        return "DoubleCheckTransform"
    }

    override fun createWeaveClassVisitor(classWriter: ClassWriter): ClassVisitor {
        return DoubleCheckModifyClassAdapter(context, classWriter)
    }

    override fun getContext(project: Project, extension: DoubleCheckExtension, android: TestedExtension): Context {
        return Context(project, extension)
    }

    override fun isNeedScanClass(): Boolean {
        return false
    }

    override fun isNeedScanWeaveRClass(): Boolean {
        return false
    }

}