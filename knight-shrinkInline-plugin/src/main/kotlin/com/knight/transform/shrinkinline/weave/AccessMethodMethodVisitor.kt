package com.knight.transform.shrinkinline.weave

import com.knight.transform.Utils.Log
import com.knight.transform.shrinkinline.Context
import org.objectweb.asm.Handle
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import java.util.ArrayList

class AccessMethodMethodVisitor(mv: MethodVisitor, val context: Context, val owner: String, val name: String, val desc: String) : MethodVisitor(Opcodes.ASM5, mv) {
    val TAG = "AccessMethodMethodVisitor";

    override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, desc: String?, isInterface: Boolean) {
        // private调用
        if (opcode == Opcodes.INVOKESPECIAL && context.isPrivateAccessMember(owner!!, name!!, desc!!)) {
            Log.d(TAG, String.format("In method( className = [%s], methodName = [%s], desc = [%s] ) code " + ",alter method( className = [%s], methodName = [%s], desc = [%s] ) invoke instruction, from INVOKESPECIAL to INVOKEVIRTUAL",
                    this.owner, this.name, this.desc, owner, name, desc))
            // This method access was changed to be public
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, owner, name, desc, isInterface)
            return
        }
        if (opcode != Opcodes.INVOKESTATIC) {
            super.visitMethodInsn(opcode, owner, name, desc, isInterface)
            return
        }

        val accessMethod = context.getAccessMethod(owner!!, name!!, desc!!)
        if (accessMethod == null) {
            super.visitMethodInsn(opcode, owner, name, desc, isInterface)
            return
        }
        Log.d(TAG, String.format("In method( className = [%s], methodName = [%s], desc = [%s] ) code " + ",inline method( className = [%s], methodName = [%s], desc = [%s] ) invoke",
                this.owner, this.name, this.desc, owner, name, desc))


        val insnNodes = accessMethod.insnNodeList

        insnNodes?.forEach {
            it.accept(this)
        }
    }

}