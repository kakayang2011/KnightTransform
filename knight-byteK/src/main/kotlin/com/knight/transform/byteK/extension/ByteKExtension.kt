package com.knight.transform.byteK.extension

import com.knight.transform.BaseExtension

open class ByteKExtension(var keepList: ArrayList<String> = ArrayList(),
                          var checkClassPath: String = "com/knight/doublecheck/library/DoubleCheckTool",
                          var checkClassAnnotation: String = "com/knight/doublecheck/library/DoubleCheck"
) : BaseExtension() {

}
