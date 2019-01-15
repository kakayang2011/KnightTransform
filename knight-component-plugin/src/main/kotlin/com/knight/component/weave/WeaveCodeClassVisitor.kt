package com.knight.modularization.weave

import com.knight.modularization.Context
import com.knight.modularization.extension.ModularizationExtension
import com.knight.transform.weave.BaseClassVisitor
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

class WeaveCodeClassVisitor(context: Context, cv: ClassVisitor) : BaseClassVisitor<Context>(context, cv) {
    val extension = context.extension as ModularizationExtension

    var isAppModule = false
    var isServiceManager = false
    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        isAppModule = descriptor == "L${extension.appPath};"
        return super.visitAnnotation(descriptor, visible)
    }

    override fun visit(version: Int, access: Int, name: String, signature: String?, superName: String?, interfaces: Array<String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        isServiceManager = name == extension.serviceManagerPath
    }


    override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        val visitMethod = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (isAppModule) {
            println("modularization: application -> $className")
            when (name + descriptor) {
                "onCreate()V" -> return AddCallAppInjectMethodVisitor(context, visitMethod, "onCreate", "()V", false, false)
                "attachBaseContext(Landroid/content/Context;)V" -> return AddCallAppInjectMethodVisitor(context, visitMethod, "attachBaseContext", "(Landroid/content/Context;)V", true, false)
                "onConfigurationChanged(Landroid/content/res/Configuration;)V" -> return AddCallAppInjectMethodVisitor(context, visitMethod, "onConfigurationChanged", "(Landroid/content/res/Configuration;)V", true, false)
                "onLowMemory()V" -> return AddCallAppInjectMethodVisitor(context, visitMethod, "onLowMemory", "()V", false, false)
                "onTerminate()V" -> return AddCallAppInjectMethodVisitor(context, visitMethod, "onTerminate", "()V", false, false)
                "onTrimMemory(I)V" -> return AddCallAppInjectMethodVisitor(context, visitMethod, "onTrimMemory", "(I)V", false, true)
            }
        }

        if (isServiceManager && access == 2 && name == "<init>" && descriptor == "()V") {
            println("modularization: serviceManager -> $className")
            return AddCodeToConstructorVisitor(context, visitMethod)
        }
        return visitMethod

    }
}