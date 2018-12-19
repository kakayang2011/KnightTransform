package com.knight.transform

import com.knight.transform.weave.DoubleCheckModifyClassAdapter
import com.knight.transform.weave.DoubleClickCheckModifyClassAdapter
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import transform.asm.BaseWeaver
import transform.task.WeavedClass

class DoubleCheckWeaver(var weaverClasses: ArrayList<WeavedClass>) : BaseWeaver() {


    override fun wrapClassWriter(classWriter: ClassWriter): ClassVisitor {
        return DoubleCheckModifyClassAdapter(classWriter)
    }

    override fun getInfomation(classVisitor: ClassVisitor) {
//        println("test::::: " + (classVisitor as DoubleClickCheckModifyClassAdapter).weavedClass.toString())
        weaverClasses.add((classVisitor as DoubleCheckModifyClassAdapter).weavedClass)
    }

}