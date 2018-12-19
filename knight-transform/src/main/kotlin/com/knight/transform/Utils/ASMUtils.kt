package transform.Utils

import org.objectweb.asm.Type

import org.objectweb.asm.Opcodes.ACC_PRIVATE
import org.objectweb.asm.Opcodes.ACC_PUBLIC
import org.objectweb.asm.Opcodes.ACC_STATIC

object ASMUtils {
    fun isPrivate(access: Int): Boolean {
        return access and ACC_PRIVATE != 0
    }

    fun isPublic(access: Int): Boolean {
        return access and ACC_PUBLIC != 0
    }

    fun isStatic(access: Int): Boolean {
        return access and ACC_STATIC != 0
    }

    fun convertSignature(name: String, desc: String): String {
        val method = Type.getType(desc)
        val sb = StringBuilder()
        sb.append(method.returnType.className).append(" ").append(name)
        sb.append("(")
        for (i in 0 until method.argumentTypes.size) {
            sb.append(method.argumentTypes[i].className)
            if (i != method.argumentTypes.size - 1) {
                sb.append(",")
            }
        }
        sb.append(")")
        return sb.toString()
    }
}