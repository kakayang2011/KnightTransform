package com.knight.transform.byteK.weave.shrinkR

import com.knight.transform.byteK.Context
import com.knight.transform.weave.BaseClassVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import transform.Utils.ASMUtils

class AnalyzeRClassVisitor(context: Context, cv: ClassVisitor) : BaseClassVisitor<Context>(context, cv) {


    private var discardable = true

    override fun visitField(access: Int, name: String, desc: String?, signature: String?, value: Any?): FieldVisitor {
        if (ASMUtils.isPublic(access) && ASMUtils.isStatic(access) && ASMUtils.isFinal(access) && ASMUtils.isInt(desc!!)
                && !context.shouldKeep(this.className, name)) {
            value?.let {
//                println(String.format("--> addvalue =[$value] field = [ %s ] in R class = [ %s ]", name, className))
                context.addRField(className, name, it)
            }
        } else {
            discardable = false
        }
        return super.visitField(access, name, desc, signature, value)
    }

    override fun visitEnd() {
        super.visitEnd()
        if (discardable) {
            context.addRClass(className)
        }
    }
}