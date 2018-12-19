package com.knight.transform

import com.knight.transform.asm.BaseWeaver
import com.knight.transform.java.DoubleClickCheckModifyClassAdapter
import com.knight.transform.java.outmap.WeavedClass
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class DoubleCheckWeaver(var weaverClasses: ArrayList<WeavedClass>) : BaseWeaver() {


    override fun wrapClassWriter(classWriter: ClassWriter): ClassVisitor {
        return DoubleClickCheckModifyClassAdapter(classWriter)
    }

    override fun getInfomation(classVisitor: ClassVisitor) {
//        println("test::::: " + (classVisitor as DoubleClickCheckModifyClassAdapter).weavedClass.toString())
        weaverClasses.add((classVisitor as DoubleClickCheckModifyClassAdapter).weavedClass)
    }

}