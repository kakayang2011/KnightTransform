package com.knight.transform.weave

import com.knight.transform.Context
import com.knight.transform.extension.DoubleCheckExtension

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

open class ViewOnClickCheckMethodVisitor(val context: Context, mv: MethodVisitor) : MethodVisitor(Opcodes.ASM5, mv) {
    var weaved: Boolean = false
    val extension = context.extension as DoubleCheckExtension
    override fun visitCode() {
        super.visitCode()
        if (weaved) return

        val annotationVisitor = mv.visitAnnotation("L" + extension.checkClassAnnotation + ";", false)
        annotationVisitor.visitEnd()

        mv.visitMethodInsn(Opcodes.INVOKESTATIC, extension.checkClassPath, "isClickable", "()Z", false)
        val l1 = Label()
        mv.visitJumpInsn(Opcodes.IFNE, l1)
        mv.visitInsn(Opcodes.RETURN)
        mv.visitLabel(l1)
    }

    override fun visitAnnotation(desc: String?, visible: Boolean): AnnotationVisitor {
        weaved = desc == "L" + extension.checkClassAnnotation + ";"
        return super.visitAnnotation(desc, visible)
    }


}