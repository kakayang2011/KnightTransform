package transform.task

import java.io.Serializable
import java.util.LinkedHashSet

class WeavedClass(val className: String) : Serializable {

    val doubleCheckMethods = LinkedHashSet<String>()


    fun addDoubleCheckMethod(methodSignature: String) {
        doubleCheckMethods.add(methodSignature)
    }


    fun getDoubleCheckMethods(): Set<String> {
        return doubleCheckMethods
    }

    fun hasDoubleCheckMethod(): Boolean {
        return doubleCheckMethods.size > 0
    }

}