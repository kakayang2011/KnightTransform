package com.knight.plugin.doublecheck

import com.google.common.io.Files
import com.knight.plugin.doublecheck.java.DoubleClickCheckModifyClassAdapter
import com.knight.plugin.doublecheck.java.WeavedClass
import groovy.transform.PackageScope
import groovy.util.logging.Slf4j
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

@Slf4j
class DoubleCheckInject {

    @PackageScope
    static void injectJar(File inputJar, File outputJar, List<WeavedClass> weavedClasses) throws IOException {
        Files.createParentDirs(outputJar)
        new ZipOutputStream(new FileOutputStream(outputJar)).withCloseable { outputStream ->

            new ZipInputStream(new FileInputStream(inputJar)).withCloseable { inputStream ->

                ZipEntry entry
                while ((entry = inputStream.nextEntry) != null) {

                    if (!entry.isDirectory() && Utils.isMatchCondition(entry.name)) {

                        byte[] newContent = visitAndReturnBytecode(entry.name,
                                IOUtils.toByteArray(inputStream), weavedClasses)

                        outputStream.putNextEntry(new ZipEntry(entry.name))
                        outputStream.write(newContent)
                        outputStream.closeEntry()
                    }
                }
            }
        }
    }

    static void injectFile(File inputFile, File outputFile, List<WeavedClass> weavedClasses) {
        println("=====input file = ${inputFile.path}")
        println("=====output file = ${outputFile.path}")
        Files.createParentDirs(outputFile)
        byte[] newContent = visitAndReturnBytecode(inputFile.name, inputFile.bytes, weavedClasses)

        outputFile.withOutputStream {
            it.write(newContent)
        }
    }

    @PackageScope
    static byte[] visitAndReturnBytecode(String name, byte[] bytes, List<WeavedClass> weavedClasses) {
        def weavedBytes = bytes

        ClassReader classReader = new ClassReader(bytes)
        ClassWriter classWriter = new ClassWriter(classReader,
                ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS)

        DoubleClickCheckModifyClassAdapter classAdapter = new DoubleClickCheckModifyClassAdapter(classWriter)
        try {
            classReader.accept(classAdapter, ClassReader.EXPAND_FRAMES)
            weavedBytes = classWriter.toByteArray()
            weavedClasses.add(classAdapter.getWeavedClass())
        } catch (Exception e) {
            println "Exception occurred when visit code \n " + e.printStackTrace()
        }

        return weavedBytes
    }
}
