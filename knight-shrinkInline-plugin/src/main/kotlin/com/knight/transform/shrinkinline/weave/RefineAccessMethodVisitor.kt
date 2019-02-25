package com.knight.transform.shrinkinline.weave

import com.knight.transform.Utils.Log
import com.knight.transform.shrinkinline.Context
import org.objectweb.asm.Handle
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import java.util.ArrayList

class RefineAccessMethodVisitor(mv: MethodVisitor, val context: Context, val accessMethodEntity: AccessMethodEntity) : MethodVisitor(Opcodes.ASM5, mv) {

    val TAG = "RefineAccessMethodVisitor"
    private val refinedInsns = ArrayList<AbstractInsnNode>()

    init {
        accessMethodEntity.insnNodeList = refinedInsns
    }

    override fun visitFieldInsn(opcode: Int, owner: String?, name: String?, descriptor: String?) {
        super.visitFieldInsn(opcode, owner, name, descriptor)
        refinedInsns.add(FieldInsnNode(opcode, owner, name, descriptor))
        Log.d(TAG, String.format("add Filed target( className = [%s], methodName = [%s], desc = [%s] ) ",
                owner, name, descriptor))
        accessMethodEntity.target = context.addAccessedMemebers(owner!!, name!!, descriptor!!, true)
    }

    override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        refinedInsns.add(MethodInsnNode(opcode, owner, name, descriptor, isInterface))
        accessMethodEntity.target = context.addAccessedMemebers(owner!!, name!!, descriptor!!, false)
        Log.d(TAG, String.format("add Method target( className = [%s], methodName = [%s], desc = [%s] ) ",
                owner, name, descriptor))
    }

    override fun visitInsn(opcode: Int) {
        super.visitInsn(opcode)
        if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
            return
        }
        refinedInsns.add(InsnNode(opcode))
    }

    override fun visitInvokeDynamicInsn(name: String?, descriptor: String?, bootstrapMethodHandle: Handle?, vararg bootstrapMethodArguments: Any?) {
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, *bootstrapMethodArguments)
        refinedInsns.add(InvokeDynamicInsnNode(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments))
    }

}