package com.knight.component.weave

import com.knight.component.Context
import com.knight.component.extension.ComponentExtension
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class AddCallAppInjectMethodVisitor(val context: Context, mv: MethodVisitor,
                                    val methodName: String, val methodDes: String,
                                    val aLoad1: Boolean, val iLoad1: Boolean) : MethodVisitor(Opcodes.ASM5, mv) {
    val extension = context.extension as ComponentExtension

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