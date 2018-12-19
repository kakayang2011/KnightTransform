package com.knight.plugin.doublecheck

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.ide.common.internal.WaitableExecutor
import com.google.common.io.Files
import com.knight.plugin.doublecheck.java.WeavedClass
import com.knight.plugin.doublecheck.transform.KnightTransform
import groovy.util.logging.Slf4j
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

import static com.google.common.base.Preconditions.checkNotNull

class DoubleCheckTransformk extends KnightTransform {
    public Map<String, List<WeavedClass>> weavedVariantClassesMap;
    private ArrayList list = new ArrayList<WeavedClass>();

    DoubleCheckTransformk(Map<String, List<WeavedClass>> weavedVariantClassesMap) {
        this.weavedVariantClassesMap = weavedVariantClassesMap;
        bytecodeWeaver = new DoubleCheckWeaver(list);
    }


    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        weavedVariantClassesMap.put(transformInvocation.getContext().getVariantName(), list);
        super.transform(transformInvocation);
    }
}
