package transform.asm

import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.nio.file.attribute.FileTime
import java.util.zip.CRC32
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

interface IWeaver {

    fun isWeaveableClass(filePath: String): Boolean

    fun weaveSingleClassToByteArray(intpuStream: InputStream): ByteArray
}

open abstract class BaseWeaver : IWeaver {
    companion object {
        val ZERO = FileTime.fromMillis(0)
    }

    override fun isWeaveableClass(filePath: String): Boolean {
        return filePath.endsWith(".class")
                && !filePath.contains("R$")
                && !filePath.contains("R.class")
                && !filePath.contains("BuildConfig.class")
    }

    override fun weaveSingleClassToByteArray(intpuStream: InputStream): ByteArray {
        val classReader = ClassReader(intpuStream)
        val classWriter = ClassWriter(classReader,
                ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
        val classVisitor = wrapClassWriter(classWriter)
        try {
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
            getInfomation(classVisitor)
        } catch (e: Exception) {
            println("Exception occurred when visit code \n " + e.printStackTrace())

        }
        return classWriter.toByteArray()
    }

    protected abstract fun getInfomation(classVisitor: ClassVisitor)

    protected abstract fun wrapClassWriter(classWriter: ClassWriter): ClassVisitor

    fun weavJar(inputJar: File, outputJar: File) {
        val inputZip = ZipFile(inputJar)
        val outputZipStream = ZipOutputStream(
                BufferedOutputStream(
                        java.nio.file.Files.newOutputStream(outputJar.toPath())))
        inputZip.entries().toList().forEach { entry ->
            val originalFile = BufferedInputStream(inputZip.getInputStream(entry))
            val outEntry = ZipEntry(entry.name)
            val newEntryContent: ByteArray
            if (!isWeaveableClass(outEntry.name.replace("/", "."))) {
                newEntryContent = org.apache.commons.io.IOUtils.toByteArray(originalFile)
            } else {
                newEntryContent = weaveSingleClassToByteArray(originalFile)
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

    fun weaveFile(inputFile: File, outputFile: File, inputDir: String) {
        var inputBaseDir = inputDir
        if (!inputBaseDir.endsWith("/")) inputBaseDir += "/"
        if (isWeaveableClass(inputFile.absolutePath.replace(inputBaseDir, "").replace("/", "."))) {
            FileUtils.touch(outputFile)
            val inputStream = FileInputStream(inputFile)
            val bytes = weaveSingleClassToByteArray(inputStream)
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