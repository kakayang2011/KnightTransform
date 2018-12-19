package com.knight.transform

import com.android.build.api.transform.TransformInvocation
import com.knight.transform.java.outmap.WeavedClass

class DoubleCheckTransform(val weavedVariantClassesMap: LinkedHashMap<String, List<WeavedClass>>) : KnightTransform() {

    val weaverClasses: ArrayList<WeavedClass> = java.util.ArrayList()

    init {
        baseWeaver = DoubleCheckWeaver(weaverClasses)
    }


    override fun transform(transform: TransformInvocation) {
        weavedVariantClassesMap[transform.context.variantName] = weaverClasses
        super.transform(transform)
    }
}