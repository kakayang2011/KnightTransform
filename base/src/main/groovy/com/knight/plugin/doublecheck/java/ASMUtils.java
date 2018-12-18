package com.knight.plugin.doublecheck.java;

import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;

public class ASMUtils {

    static boolean isPrivate(int access) {
        return (access & ACC_PRIVATE) != 0;
    }

    static boolean isPublic(int access) {
        return (access & ACC_PUBLIC) != 0;
    }

    static boolean isStatic(int access) {
        return (access & ACC_STATIC) != 0;
    }

    static String convertSignature(String name, String desc) {
        Type method = Type.getType(desc);
        StringBuilder sb = new StringBuilder();
        sb.append(method.getReturnType().getClassName()).append(" ").append(name);
        sb.append("(");
        for (int i = 0; i < method.getArgumentTypes().length; i++) {
            sb.append(method.getArgumentTypes()[i].getClassName());
            if (i != method.getArgumentTypes().length - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
