package com.knight.transform.java.outmap

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.lang.StringBuilder

open class OutPutMappingTask : DefaultTask() {
    init {
        group = "doubleCheck"
        description = "write double check mapping file"
    }

    @Input
    var variantName = project.objects.property(String::class.java)

    @OutputFile
    var outputMappingFile = newOutputFile()

    @Internal
    var classes = project.objects.property(LinkedHashMap::class.java)

    @TaskAction
    fun writeMapping() {
        var loggable = true
        val mappingFile = outputMappingFile.get().asFile

        FileUtils.touch(mappingFile)
        val content = StringBuilder()
        (classes.get()[variantName.get()] as List<WeavedClass>).forEach {
            it.takeIf {
                it.hasDoubleCheckMethod()
            }?.let {
                val className = it.className
                val doubleCheckMethods = it.doubleCheckMethods
                content.append(className).append("\n")
                if (loggable) println(className)
                doubleCheckMethods.forEach {
                    content.append("\u21E2 $it").append("\n")
                    if (loggable) println("\u21E2 $it")
                }
            }
        }
        mappingFile.writeText(content.toString())
        println("Success wrote TXT mapping report to file://${outputMappingFile}")

    }

}