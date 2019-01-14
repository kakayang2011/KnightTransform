package com.knight.modularization

import com.android.build.gradle.TestedExtension
import com.knight.modularization.extension.ModularizationExtension
import com.knight.modularization.weave.FindTargetClassAdapter
import com.knight.transform.KnightPlugin
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class ModularizationPlugin : KnightPlugin<ModularizationExtension, Context>() {


    private val EXTENSION_NAME = "modularizationConfig"
    override val isNeedPrintMapAndTaskCostTime: Boolean = true

    override fun createExtensions(): ModularizationExtension {
        project.extensions.create(EXTENSION_NAME, ModularizationExtension::class.java)
        (project.extensions.getByName(EXTENSION_NAME) as ModularizationExtension).apply {
            context = Context(project, this)
            return this
        }
    }

    override fun createScanClassVisitor(classWriter: ClassWriter): ClassVisitor? {
        return null
    }

    override fun getTransformName(): String {
        return "ModularizationTransform"
    }

    override fun createWeaveClassVisitor(classWriter: ClassWriter): ClassVisitor {
        return FindTargetClassAdapter(context, classWriter)
    }

    override fun getContext(project: Project, extension: ModularizationExtension, android: TestedExtension): Context {
        return Context(project, extension)
    }

    override fun isNeedScanClass(): Boolean {
        return false
    }

    override fun isNeedScanWeaveRClass(): Boolean {
        return false
    }

}