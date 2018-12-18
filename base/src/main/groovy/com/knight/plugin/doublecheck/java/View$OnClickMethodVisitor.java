package com.knight.plugin.doublecheck.java;

import com.knight.plugin.doublecheck.DoubleCheckConfig;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.ASMifier;

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
                mv.visitAnnotation("L" + DoubleCheckConfig.checkClassAnnotation + ";", false);
        annotationVisitor.visitEnd();

        mv.visitMethodInsn(Opcodes.INVOKESTATIC, DoubleCheckConfig.checkClassPath, "isClickable", "()Z", false);
        Label l1 = new Label();
        mv.visitJumpInsn(Opcodes.IFNE, l1);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitLabel(l1);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        /*Lcom/smartdengg/clickdebounce/Debounced;*/
        weaved = desc.equals("L" + DoubleCheckConfig.checkClassAnnotation + ";");
        return super.visitAnnotation(desc, visible);
    }

}
