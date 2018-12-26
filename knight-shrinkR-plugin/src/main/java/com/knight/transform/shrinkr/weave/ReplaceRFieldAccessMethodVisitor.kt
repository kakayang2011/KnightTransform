package com.knight.transform.shrinkr.weave

import com.knight.transform.shrinkr.Context
import org.objectweb.asm.*
import transform.Utils.ASMUtils
import transform.task.WeavedClass

open class ReplaceRFieldAccessMethodVisitor(val context: Context, val methodName: String,
                                            val className: String, mv: MethodVisitor, val weavedClass: WeavedClass) : MethodVisitor(Opcodes.ASM5, mv) {
    private var processingLineNumber: Int = 0
    override fun visitFieldInsn(opcode: Int, owner: String, name: String, desc: String) {
        if (opcode == Opcodes.GETSTATIC) {
            val value = context.getRFieldValue(owner, name)
//            println("value: $value")
            if (value != null) {
//                println("getvalue: $value")
                visitLdcInsn(value)
                return
            }
        }
        super.visitFieldInsn(opcode, owner, name, desc)
    }

    override fun visitLineNumber(line: Int, start: Label) {
        processingLineNumber = line
        super.visitLineNumber(line, start)
    }

    override fun visitLdcInsn(value: Any) {
        if (value is Type) {
            val sort = value.sort
            if (sort == Type.OBJECT) {
                val rClassName = value.internalName
                if (context.isRClass(rClassName)) {
                    val sb = StringBuilder()
                    val msg = String.format("R class = [ %s ] may be references by reflect api, please check if it has bean kept.\n", rClassName)
                    sb.append(msg)
                            .append(String.format("             at %s.%s(%s.java:%s) \n",
                                    className.replace("/".toRegex(), "."), methodName, className.replace("/".toRegex(), "."), processingLineNumber.toString()))
                    println(sb.toString())
                }
            }
        } else if (value is String) {
            if (value.length > 1 && context.isRClassName(value)) {
                val sb = StringBuilder()
                val msg = String.format("R class = [ %s ] may be references by reflect api, please check if it has bean kept.\n", value)
                sb.append(msg)
                        .append(String.format("             at %s.%s(%s.java:%s) \n",
                                className.replace("/".toRegex(), "."), methodName, className.replace("/".toRegex(), "."), processingLineNumber.toString()))
                println(sb.toString())
            }
        }
        super.visitLdcInsn(value)
    }

}