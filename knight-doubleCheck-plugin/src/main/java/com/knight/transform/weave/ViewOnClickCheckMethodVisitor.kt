package com.knight.transform.weave

import com.knight.transform.java.outmap.KnightConfigManager

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

open class ViewOnClickCheckMethodVisitor(mv: MethodVisitor) : MethodVisitor(Opcodes.ASM5, mv) {
    var weaved: Boolean = false
    override fun visitCode() {
        super.visitCode()
        if (weaved) return

        val annotationVisitor = mv.visitAnnotation("L" + KnightConfigManager.knightConfig.checkClassAnnotation + ";", false)
        annotationVisitor.visitEnd()

        mv.visitMethodInsn(org.objectweb.asm.Opcodes.INVOKESTATIC, KnightConfigManager.knightConfig.checkClassPath, "isClickable", "()Z", false)
        val l1 = Label()
        mv.visitJumpInsn(org.objectweb.asm.Opcodes.IFNE, l1)
        mv.visitInsn(org.objectweb.asm.Opcodes.RETURN)
        mv.visitLabel(l1)
    }

    override fun visitAnnotation(desc: String?, visible: Boolean): AnnotationVisitor {
        weaved = desc == "L" + KnightConfigManager.knightConfig.checkClassAnnotation + ";"
        return super.visitAnnotation(desc, visible)
    }

}