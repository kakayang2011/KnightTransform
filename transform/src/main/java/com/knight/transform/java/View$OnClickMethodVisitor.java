package com.knight.transform.java;


import com.knight.transform.java.outmap.KnightConfigManager;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class View$OnClickMethodVisitor extends MethodVisitor {
    private boolean weaved;

    public View$OnClickMethodVisitor(MethodVisitor mv) {
        super(Opcodes.ASM5, mv);

    }


    @Override
    public void visitCode() {
        super.visitCode();
        if (weaved) return;

        AnnotationVisitor annotationVisitor =
                mv.visitAnnotation("L" + KnightConfigManager.INSTANCE.getKnightConfig().getCheckClassAnnotation() + ";", false);
        annotationVisitor.visitEnd();

        mv.visitMethodInsn(Opcodes.INVOKESTATIC, KnightConfigManager.INSTANCE.getKnightConfig().getCheckClassPath(), "isClickable", "()Z", false);
        Label l1 = new Label();
        mv.visitJumpInsn(Opcodes.IFNE, l1);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitLabel(l1);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        /*Lcom/smartdengg/clickdebounce/Debounced;*/
        weaved = desc.equals("L" + KnightConfigManager.INSTANCE.getKnightConfig().getCheckClassAnnotation() + ";");
        return super.visitAnnotation(desc, visible);
    }

}
