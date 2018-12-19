package com.knight.transform.java;

import com.knight.transform.java.outmap.WeavedClass;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DoubleClickCheckModifyClassAdapter extends ClassVisitor implements Opcodes {
    private WeavedClass weavedClass;

    public DoubleClickCheckModifyClassAdapter(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);

    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        weavedClass = new WeavedClass(name);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
        if ((ASMUtils.isPublic(access) && !ASMUtils.isStatic(access)) && //
                name.equals("onClick") && //
                desc.equals("(Landroid/view/View;)V")) {
            methodVisitor = new View$OnClickMethodVisitor(methodVisitor);
            System.out.println("--->name: " + name);
            weavedClass.addDoubleCheckMethod(ASMUtils.convertSignature(name, desc));
        }

        return methodVisitor;
    }

    public WeavedClass getWeavedClass() {
        return weavedClass;
    }

}
