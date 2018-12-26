package com.knight.transform.extension

import com.knight.transform.BaseExtension

open class DoubleCheckExtension(var checkClassPath: String = "com/knight/doublecheck/library/DoubleCheckTool",
                                var checkClassAnnotation: String = "com/knight/doublecheck/library/DoubleCheck"
) : BaseExtension()

