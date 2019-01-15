package com.knight.modularization.weave

import com.knight.modularization.Context
import com.knight.modularization.extension.ModularizationExtension
import com.knight.transform.weave.BaseClassVisitor
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import transform.Utils.ASMUtils

open class FindTargetClassAdapter(context: Context, cv: ClassVisitor) : BaseClassVisitor<Context>(context, cv) {

    var interfaces: Array<String>? = null
    val extension = context.extension as ModularizationExtension

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