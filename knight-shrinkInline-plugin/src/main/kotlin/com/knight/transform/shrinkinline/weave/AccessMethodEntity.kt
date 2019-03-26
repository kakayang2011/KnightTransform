package com.knight.transform.shrinkinline.weave

import com.android.tools.r8.ir.code.MemberType
import com.knight.transform.MemberEntity
import jdk.internal.org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.MethodInsnNode

class AccessMethodEntity(className: String, name: String, desc: String) : MemberEntity(Opcodes.ACC_STATIC, className, name, desc) {

    var target: MemberEntity? = null
    var insnNodeList: ArrayList<AbstractInsnNode>? = null


    fun getMethodInsn(): MethodInsnNode? {
        if (target?.type == MemberEntity.FIELD) {
            return null
        }

        insnNodeList?.forEach {
            if (it is MethodInsnNode) {
                return it
            }
        }
        return null
    }

}
//
//
//class RefMemberEntity<T : MemberEntity>(private val origin: T) : MemberEntity(origin.access, origin.className, origin.name, origin.desc) {
//
//    fun setAccess1(access: Int) {
//        origin.setAccess(access)
//    }
//
//    fun type(): MemberType {
//        return origin.type()
//    }
//
//    fun access(): Int {
//        return origin.access
//    }
//
//    fun className(): String {
//        return origin.className
//    }
//
//    fun name(): String {
//        return origin.name
//    }
//
//    fun desc(): String {
//        return origin.desc
//    }
//
//    override fun hashCode(): Int {
//        return origin.hashCode()
//    }
//
//    override fun toString(): String {
//        return origin.toString()
//    }
//}