package com.knight.transform.weave

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import transform.Utils.ASMUtils
import transform.task.WeavedClass

open class DoubleCheckModifyClassAdapter(cv: ClassVisitor) : ClassVisitor(Opcodes.ASM5, cv), Opcodes {
    lateinit var weavedClass: WeavedClass

    override fun visit(version: Int, access: Int, name: String, signature: String?, superName: String?, interfaces: Array<String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        weavedClass = WeavedClass(name)
    }

    override fun visitMethod(access: Int, name: String?, desc: String?, signature: String?, exceptions: Array<String>?): MethodVisitor {
        var methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions)
        if (ASMUtils.isPublic(access) && !ASMUtils.isStatic(access) && //
                name == "onClick" && //
                desc == "(Landroid/view/View;)V") {
            methodVisitor = ViewOnClickCheckMethodVisitor(methodVisitor)
            println("--->name: $name")
            weavedClass.addDoubleCheckMethod(ASMUtils.convertSignature(name, desc))
        }

        return methodVisitor
    }


}