package com.knight.modularization.weave

import com.knight.modularization.Context
import com.knight.modularization.extension.ModularizationExtension

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

open class FindTargetClassMethodVisitor(val context: Context, mv: MethodVisitor,
                                        val className: String, val interfaces: Array<String>?) : MethodVisitor(Opcodes.ASM5, mv) {
    var weaved: Boolean = false
    val extension = context.extension as ModularizationExtension

    override fun visitAnnotation(desc: String?, visible: Boolean): AnnotationVisitor {

        when (desc) {
            extension.modulePath -> {
                context.moduleApplications.add(className)
            }
            extension.serviceProvider -> {
                interfaces?.forEach {
                    context.serviceImpl[it] = className
                }
            }
        }
        return super.visitAnnotation(desc, visible)
    }


}