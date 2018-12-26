package com.knight.transform

import com.knight.transform.asm.IWeaver
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

interface IPlugin {
    fun createWeaveClassVisitor(classWriter: ClassWriter): ClassVisitor

    fun createScanClassVisitor(classWriter: ClassWriter): ClassVisitor?

    fun isNeedScanClass(): Boolean

    fun isNeedScanWeaveRClass(): Boolean

}