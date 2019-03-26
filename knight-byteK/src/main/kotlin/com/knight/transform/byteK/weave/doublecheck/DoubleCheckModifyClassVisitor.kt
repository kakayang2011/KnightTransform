package com.knight.transform.byteK.weave.doublecheck

import com.knight.transform.byteK.Context
import com.knight.transform.weave.BaseClassVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import transform.Utils.ASMUtils

open class DoubleCheckModifyClassVisitor(context: Context, cv: ClassVisitor) : BaseClassVisitor<Context>(context, cv), Opcodes {
    override fun visitMethod(access: Int, name: String?, desc: String?, signature: String?, exceptions: Array<String>?): MethodVisitor {
        var methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions)
        if (ASMUtils.isPublic(access) && !ASMUtils.isStatic(access) && //
                name == "onClick" && //
                desc == "(Landroid/view/View;)V") {
            methodVisitor = DoubleCheckMethodVisitor(context, methodVisitor)

//            weavedClass.addDoubleCheckMethod(ASMUtils.convertSignature(name, desc))
//            println("--->name: $name")
            weavedClass.addWeavedMethod(ASMUtils.convertSignature(name, desc))
        }

        return methodVisitor
    }


}