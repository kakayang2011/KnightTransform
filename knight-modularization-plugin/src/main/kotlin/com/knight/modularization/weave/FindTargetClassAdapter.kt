package com.knight.modularization.weave

import com.knight.modularization.Context
import com.knight.transform.weave.BaseClassVisitor
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import transform.Utils.ASMUtils

open class FindTargetClassAdapter(context: Context, cv: ClassVisitor) : BaseClassVisitor<Context>(context, cv) {

    var interfaces: Array<String>? = null

    override fun visit(version: Int, access: Int, name: String, signature: String?, superName: String?, interfaces: Array<String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.interfaces = interfaces
    }

    override fun visitMethod(access: Int, name: String?, desc: String?, signature: String?, exceptions: Array<String>?): MethodVisitor {
        val methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions)
        FindTargetClassMethodVisitor(context, methodVisitor, className, interfaces)
        return cv.visitMethod(access, name, desc, signature, exceptions)
    }
}