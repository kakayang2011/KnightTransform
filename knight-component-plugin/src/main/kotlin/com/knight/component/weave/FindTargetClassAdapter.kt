package com.knight.component.weave

import com.knight.component.Context
import com.knight.component.extension.ComponentExtension
import com.knight.transform.weave.BaseClassVisitor
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor

open class FindTargetClassAdapter(context: Context, cv: ClassVisitor) : BaseClassVisitor<Context>(context, cv) {

    var interfaces: Array<String>? = null
    val extension = context.extension as ComponentExtension

    override fun visit(version: Int, access: Int, name: String, signature: String?, superName: String?, interfaces: Array<String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.interfaces = interfaces
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        when (descriptor) {
            "L${extension.modulePath};" -> {
                println("modularization: modulePath -> $className")
                context.moduleApplications.add(className)
            }
            "L${extension.serviceImpl};" -> {
                interfaces?.forEach {
                    println("modularization: moduleImpl -> $className")
                    context.serviceImpl[it] = className
                }
            }
        }
        return super.visitAnnotation(descriptor, visible)
    }
}