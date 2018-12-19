package com.knight.plugin.doublecheck;

import com.android.build.api.transform.Context;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.knight.plugin.doublecheck.java.WeavedClass;
import com.knight.plugin.doublecheck.transform.KnightTransform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DoubleCheckTransformj extends KnightTransform {

    public Map<String, List<WeavedClass>> weavedVariantClassesMap;
    private ArrayList list = new ArrayList<WeavedClass>();

    DoubleCheckTransformj(Map<String, List<WeavedClass>> weavedVariantClassesMap) {
        this.weavedVariantClassesMap = weavedVariantClassesMap;
        bytecodeWeaver = new DoubleCheckWeaver(list);
    }


    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
//        weavedVariantClassesMap.put(transformInvocation.getContext().getVariantName(), list);
        super.transform(transformInvocation);
    }

}
