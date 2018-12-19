package com.knight.transform.extension

open class KnightConfig(var checkClassPath: String = "com/knight/doublecheck/library/DoubleCheckTool",
                        var checkClassAnnotation: String = "com/knight/doublecheck/library/DoubleCheck",
                        var scanJar: Boolean = true)


object KnightConfigManager {
    val knightConfig by lazy(LazyThreadSafetyMode.NONE) {
        KnightConfig()
    }
}