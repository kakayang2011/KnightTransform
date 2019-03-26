package com.knight.transform.shrinkinline.weave

import com.knight.transform.Utils.Log
import com.knight.transform.Utils.TypeUtil
import com.knight.transform.shrinkinline.Context
import com.knight.transform.weave.BaseClassVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import transform.Utils.ASMUtils

class AnalyzeInlineClassVisitor(context: Context, cv: ClassVisitor) : BaseClassVisitor<Context>(context, cv) {
    val TAG = "AnalyzeInlineClassVisitor"

    override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)

        if (ASMUtils.isSelfFile(className) && TypeUtil.isSynthetic(access) && TypeUtil.isStatic(access) && name!!.startsWith("access$")) {
            Log.i(String.format("find method( className = [%s], methodName = [%s], desc = [%s] ) access, from [%s] to be find",
                    className, name, descriptor, access.toString()))

            val methodEntity = context.addAccessMethod(className, name, descriptor!!)
            return RefineAccessMethodVisitor(mv, context, methodEntity)
        }
        return mv
    }


}