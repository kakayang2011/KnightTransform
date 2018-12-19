package com.knight.transform

import com.android.build.api.transform.TransformInvocation
import com.knight.transform.extension.KnightConfigManager
import com.knight.transform.weave.DoubleCheckWeaver
import org.gradle.api.Project
import transform.KnightTransform
import transform.task.WeavedClass

class DoubleCheckTransform(val weavedVariantClassesMap: LinkedHashMap<String, List<WeavedClass>>
                           , project: Project) : KnightTransform() {


    val weaverClasses: ArrayList<WeavedClass> = java.util.ArrayList()

    init {
        baseWeaver = DoubleCheckWeaver(weaverClasses)
    }


    override fun transform(transform: TransformInvocation) {
        weavedVariantClassesMap[transform.context.variantName] = weaverClasses
        super.transform(transform)
    }

    override fun isNeedScanJar(): Boolean {
        println("============2scanJar: ${KnightConfigManager.knightConfig.scanJar}")
        return KnightConfigManager.knightConfig.scanJar
    }
}