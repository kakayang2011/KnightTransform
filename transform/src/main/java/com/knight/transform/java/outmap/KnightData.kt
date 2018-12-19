package com.knight.transform.java.outmap

open class KnightConfig(var checkClassPath: String = "com/example/knight/doublecheck/app/DoubleCheckTool",
                        var checkClassAnnotation: String = "com/example/knight/doublecheck/app/DoubleCheck",
                        var isScanJar: Boolean = true)


object KnightConfigManager {
    val knightConfig by lazy(LazyThreadSafetyMode.NONE) {
        KnightConfig()
    }
}