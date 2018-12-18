package com.knight.plugin.doublecheck

import com.knight.plugin.doublecheck.java.WeavedClass
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class OutPutMappingTask extends DefaultTask {
    {
        group = 'doubleCheck'
        description = 'write debounced mapping file'
    }

    @Input
    Property<String> variantName = project.objects.property(String.class)

    @OutputFile
    RegularFileProperty outputMappingFile = newOutputFile()

    @Internal
    Property<Map> classes = project.objects.property(Map.class)

    @TaskAction
    void wrireMapping() {
        def loggable = true
        def mappingFile = outputMappingFile.get().asFile
        FileUtils.touch(mappingFile)

        mappingFile.withWriter { writer ->

            classes.get()[variantName.get()].findAll { WeavedClass weavedClass ->
                if (weavedClass != null) {
                    weavedClass.hasDoubleCheckMethod()
                }
            }.each { touchedWeavedClass ->

                String className = touchedWeavedClass.className
                Set<String> doubleCheckMethods = touchedWeavedClass.getDoubleCheckMethods()
                writer.writeLine "$className"

                if (loggable) println className

                for (def methodSignature in doubleCheckMethods) {
                    writer.writeLine "    \u21E2  $methodSignature"
                    if (loggable) println "    \u21E2  $methodSignature"
                }
            }
        }
        println "Success wrote TXT mapping report to file://${outputMappingFile}"
    }
}
