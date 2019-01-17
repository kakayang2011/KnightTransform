package com.knight.component.weave

import com.knight.component.Context
import com.knight.component.extension.ComponentExtension
import com.knight.transform.weave.BaseClassVisitor
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

class WeaveCodeClassVisitor(context: Context, cv: ClassVisitor) : BaseClassVisitor<Context>(context, cv) {
    val extension = context.extension as ComponentExtension

    var isAppModule = false
    var isServiceManager = false
    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        isAppModule = descriptor == "L${extension.appPath};"//通过注解判断是否为@AppSpec目标类
        return super.visitAnnotation(descriptor, visible)
    }

    override fun visit(version: Int, access: Int, name: String, signature: String?, superName: String?, interfaces: Array<String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        isServiceManager = name == extension.serviceManagerPath//是否为ServiceManager
    }


    override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        val visitMethod = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (isAppModule) {//找到主app的各个方法，对其进行相应的代码织入
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

        if (isServiceManager && access == 2 && name == "<init>" && descriptor == "()V") {//找到目标类的私有构造方法
            println("modularization: serviceManager -> $className")
            return AddCodeToConstructorVisitor(context, visitMethod)
        }
        return visitMethod

    }
}