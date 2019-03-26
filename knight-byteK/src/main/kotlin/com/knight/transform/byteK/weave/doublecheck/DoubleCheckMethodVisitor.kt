package com.knight.transform.byteK.weave.doublecheck

import com.knight.transform.byteK.Context
import com.knight.transform.byteK.extension.ByteKExtension
import org.objectweb.asm.*

open class DoubleCheckMethodVisitor(val context: Context, mv: MethodVisitor) : MethodVisitor(Opcodes.ASM5, mv) {
    var weaved: Boolean = false
    val extension = context.extension as ByteKExtension
    override fun visitCode() {
        super.visitCode()
        if (weaved) return

        val annotationVisitor = mv.visitAnnotation("L" + extension.checkClassAnnotation + ";", false)
        annotationVisitor.visitEnd()

        mv.visitMethodInsn(org.objectweb.asm.Opcodes.INVOKESTATIC, extension.checkClassPath, "isClickable", "()Z", false)
        val l1 = Label()
        mv.visitJumpInsn(org.objectweb.asm.Opcodes.IFNE, l1)
        mv.visitInsn(org.objectweb.asm.Opcodes.RETURN)
        mv.visitLabel(l1)
    }

    override fun visitAnnotation(desc: String?, visible: Boolean): AnnotationVisitor {
        weaved = desc == "L" + extension.checkClassAnnotation + ";"
        return super.visitAnnotation(desc, visible)
    }

}