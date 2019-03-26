package com.knight.component.weave

import com.knight.component.Context
import com.knight.component.extension.ComponentExtension
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

class AddCodeToConstructorVisitor(val context: Context, mv: MethodVisitor) : MethodVisitor(Opcodes.ASM5, mv) {
    val extension = context.extension as ComponentExtension


    override fun visitInsn(opcode: Int) {

        when (opcode) {
            Opcodes.IRETURN,
            Opcodes.FRETURN,
            Opcodes.ARETURN,
            Opcodes.LRETURN,
            Opcodes.DRETURN,
            Opcodes.RETURN -> {
                context.moduleApplications.forEach {
                    insertApplicationAdd(it)
                }
                context.serviceImpl.forEach { t, u ->
                    insertRoutersPut(t, u)
                }
            }
        }
        super.visitInsn(opcode)
    }

    private fun insertApplicationAdd(applicationName: String) {
        println("modularization: insertApplicationAdd -> $applicationName ")
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitFieldInsn(Opcodes.GETFIELD, extension.serviceManagerPath, extension.serviceApplicationListName, "Ljava/util/ArrayList;")
        mv.visitTypeInsn(Opcodes.NEW, applicationName)
        mv.visitInsn(Opcodes.DUP)
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, applicationName, "<init>", "()V", false)
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", false)
        mv.visitInsn(Opcodes.POP)
    }

    private fun insertRoutersPut(router: String, impl: String) {
        println("modularization: insertCode -> $router ; $impl")
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitFieldInsn(Opcodes.GETFIELD, extension.serviceManagerPath, extension.serviceImplMapName, "Ljava/util/HashMap;")
        mv.visitLdcInsn(Type.getObjectType(router))
        mv.visitLdcInsn(Type.getObjectType(impl))
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false)
        mv.visitInsn(Opcodes.POP)
    }
}
