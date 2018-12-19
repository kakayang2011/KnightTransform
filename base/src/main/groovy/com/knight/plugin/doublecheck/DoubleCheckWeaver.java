package com.knight.plugin.doublecheck;

import com.knight.plugin.doublecheck.java.DoubleClickCheckModifyClassAdapter;
import com.knight.plugin.doublecheck.java.WeavedClass;
import com.knight.plugin.doublecheck.transform.BaseWeaver;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DoubleCheckWeaver extends BaseWeaver {

    private ArrayList<WeavedClass> weavedClasses;
    private DoubleClickCheckModifyClassAdapter adapter;

    public DoubleCheckWeaver(ArrayList<WeavedClass> weavedClasses) {
        this.weavedClasses = weavedClasses;
    }

    @Override
    protected ClassVisitor wrapClassWriter(ClassWriter classWriter) {
        adapter = new DoubleClickCheckModifyClassAdapter(classWriter);
        return adapter;
    }

    @Override
    public byte[] weaveSingleClassToByteArray(InputStream inputStream) throws IOException {
        byte[] bytes = super.weaveSingleClassToByteArray(inputStream);
        weavedClasses.add(adapter.getWeavedClass());
        return bytes;
    }
}
