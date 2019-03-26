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
import transform.task.WeavedClass

open class ShrinkInlineClassVisitor(context: Context, cv: ClassVisitor) : BaseClassVisitor<Context>(context, cv), Opcodes {

    val TAG = "ShrinkInlineClassVisitor"

    override fun visitField(ac: Int, name: String?, descriptor: String?, signature: String?, value: Any?): FieldVisitor {
        var access = ac
        if (context.isAccessMember(className, name!!, descriptor!!)) {
            if (TypeUtil.isPrivate(access)) {
                access = access and Opcodes.ACC_PRIVATE.inv()
            } else if (TypeUtil.isProtected(access)) {
                access = access and Opcodes.ACC_PROTECTED.inv()
                access = access or Opcodes.ACC_PUBLIC
            }

            Log.i(String.format("Change Field( className = [%s], methodName = [%s], desc = [%s] ) access,  from [%s] to  be not private",
                    className, name, descriptor, ac))

        }
        return super.visitField(access, name, descriptor, signature, value)
    }


    override fun visitMethod(ac: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor? {
        var access = ac
        if (context.isAccessMethod(className, name!!, descriptor!!)) {
            Log.d(String.format("Access$ method : className = [%s], methodName = [%s], desc = [%s]", className, name, descriptor))
            return null
        }
        if (context.isAccessMember(className, name, descriptor)) {
            if (TypeUtil.isPrivate(access)) {
                access = access and Opcodes.ACC_PRIVATE.inv()
            } else if (TypeUtil.isProtected(access)) {
                access = access and Opcodes.ACC_PROTECTED.inv()
                access = access or Opcodes.ACC_PUBLIC
            }
            Log.i(String.format("Change method( className = [%s], methodName = [%s], desc = [%s] ) access, from [%s] to be not private",
                    className, name, descriptor, ac))
        }
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)

        return AccessMethodMethodVisitor(mv, context, className, name, descriptor)
    }

}