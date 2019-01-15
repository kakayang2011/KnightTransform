package com.knight.modularization.weave

import com.knight.modularization.Context
import com.knight.modularization.extension.ModularizationExtension
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class AddCallAppInjectMethodVisitor(val context: Context, mv: MethodVisitor,
                                    val methodName: String, val methodDes: String,
                                    val aLoad1: Boolean, val iLoad1: Boolean) : MethodVisitor(Opcodes.ASM5, mv) {
    val extension = context.extension as ModularizationExtension

    override fun visitInsn(opcode: Int) {
        when (opcode) {
            Opcodes.IRETURN,
            Opcodes.FRETURN,
            Opcodes.ARETURN,
            Opcodes.LRETURN,
            Opcodes.DRETURN,
            Opcodes.RETURN -> {
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, extension.serviceManagerPath, "getIns", "()L${extension.serviceManagerPath};", false)
                if (aLoad1) {
                    mv.visitVarInsn(Opcodes.ALOAD, 1)
                }
                if (iLoad1) {
                    mv.visitVarInsn(Opcodes.ILOAD, 1)
                }
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, extension.serviceManagerPath, methodName, methodDes, false)
            }
        }
        super.visitInsn(opcode)
    }

}