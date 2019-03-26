package com.knight.transform.byteK.weave.shrinkR

import com.knight.transform.byteK.Context
import com.knight.transform.weave.BaseClassVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

open class ShrinkRClassVisitor(context: Context, cv: ClassVisitor) : BaseClassVisitor<Context>(context, cv), Opcodes {
    private var isRClass: Boolean = false

    override fun visit(version: Int, access: Int, name: String, signature: String?, superName: String?, interfaces: Array<String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
        this.isRClass = context.isRClass(name)
    }

    override fun visitField(access: Int, name: String?, desc: String?, signature: String?, value: Any?): FieldVisitor? {
        if (isRClass && context.containRField(className, name!!)/* && !context.shouldKeep(className, name)*/) {
//            println(String.format("Delete field = [ %s ] in R class = [ %s ]", name, className))
            return null
        } else if (isRClass) {
//            println(String.format("Keep field = [ %s ] in R class = [ %s ]", name, className))
        }
        return super.visitField(access, name, desc, signature, value)
    }

    override fun visitMethod(access: Int, name: String?, desc: String?, signature: String?, exceptions: Array<String>?): MethodVisitor {
        var methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions)
        if (!isRClass && name != null) {
            methodVisitor = ReplaceRFieldAccessMethodVisitor(context, name, className, methodVisitor, weavedClass)
//            println(String.format("--> field = [ %s ] in R class = [ %s ]", name, className))
//            weavedClass.addWeavedMethod(ASMUtils.convertSignature(name, desc!!))
        }

        return methodVisitor
    }


}