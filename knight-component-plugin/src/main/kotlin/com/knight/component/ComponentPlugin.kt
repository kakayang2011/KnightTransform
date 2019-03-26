package com.knight.component

import com.android.build.gradle.TestedExtension
import com.knight.component.extension.ComponentExtension
import com.knight.component.weave.FindTargetClassAdapter
import com.knight.component.weave.WeaveCodeClassVisitor
import com.knight.transform.KnightPlugin
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class ComponentPlugin : KnightPlugin<ComponentExtension, Context>() {


    private val EXTENSION_NAME = "componentConfig"
    override val isNeedPrintMapAndTaskCostTime: Boolean = true

    override fun createExtensions(): ComponentExtension {
        project.extensions.create(EXTENSION_NAME, ComponentExtension::class.java)
        (project.extensions.getByName(EXTENSION_NAME) as ComponentExtension).apply {
            context = Context(project, this)
            return this
        }
    }

    override fun createScanClassVisitor(classWriter: ClassWriter): ClassVisitor? {
        return FindTargetClassAdapter(context, classWriter)
    }

    override fun getTransformName(): String {
        return "ComponentTransform"
    }

    override fun createWeaveClassVisitor(classWriter: ClassWriter): ClassVisitor {
        return WeaveCodeClassVisitor(context, classWriter)
    }


    override fun getContext(project: Project, extension: ComponentExtension, android: TestedExtension): Context {
        return Context(project, extension)
    }

    override fun isNeedScanClass(): Boolean {
        return true
    }

    override fun isNeedScanWeaveRClass(): Boolean {
        return false
    }

}