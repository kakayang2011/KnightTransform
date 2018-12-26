package com.knight.transform.asm

import com.knight.transform.weave.ExtendClassWriter
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import java.io.*
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.attribute.FileTime
import java.util.zip.CRC32
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream


class CodeWeaver(private val wrapClassWriter: (ClassWriter) -> ClassVisitor) : IWeaver() {


    private val ZERO = FileTime.fromMillis(0)!!

    fun weaveClassToByteArray(intpuStream: InputStream): ByteArray {
        val classReader = ClassReader(intpuStream)
        val classWriter = ExtendClassWriter(classloader, classReader,
                ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
        val classVisitor = wrapClassWriter.invoke(classWriter)
        try {
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        } catch (e: Exception) {
            println("Exception occurred when visit code \n " + e.printStackTrace())

        }
        return classWriter.toByteArray()
    }


    override fun weaveJar(inputJar: File, outputJar: File) {
        val inputZip = ZipFile(inputJar)
        val outputZipStream = ZipOutputStream(BufferedOutputStream(Files.newOutputStream(outputJar.toPath())))
        inputZip.entries().toList().forEach { entry ->
            val originalFile = BufferedInputStream(inputZip.getInputStream(entry))
            val outEntry = ZipEntry(entry.name)
            val newEntryContent: ByteArray
            if (!isWeaveableClass(outEntry.name.replace("/", "."))) {
                newEntryContent = IOUtils.toByteArray(originalFile)
            } else {
                newEntryContent = weaveClassToByteArray(originalFile)
            }
            val crc32 = CRC32()
            crc32.update(newEntryContent)
            outEntry.crc = crc32.value
            outEntry.method = (ZipEntry.STORED)
            outEntry.size = (newEntryContent.size.toLong())
            outEntry.compressedSize = (newEntryContent.size.toLong())
            outEntry.lastAccessTime = (ZERO)
            outEntry.lastModifiedTime = (ZERO)
            outEntry.creationTime = (ZERO)
            outputZipStream.putNextEntry(outEntry)
            outputZipStream.write(newEntryContent)
            outputZipStream.closeEntry()
        }
        outputZipStream.flush()
        outputZipStream.close()
    }

    override fun weaveFile(inputFile: File, outputFile: File, inputDir: String) {
        var inputBaseDir = inputDir
        if (!inputBaseDir.endsWith("/")) inputBaseDir += "/"
        if (isWeaveableClass(inputFile.absolutePath.replace(inputBaseDir, "").replace("/", "."))) {
            FileUtils.touch(outputFile)
            val inputStream = FileInputStream(inputFile)
            val bytes = weaveClassToByteArray(inputStream)
            val fos = FileOutputStream(outputFile)
            fos.write(bytes)
            fos.close()
            inputStream.close()
        } else {
            if (inputFile.isFile) {
                FileUtils.touch(outputFile)
                FileUtils.copyFile(inputFile, outputFile)
            }
        }
    }

}