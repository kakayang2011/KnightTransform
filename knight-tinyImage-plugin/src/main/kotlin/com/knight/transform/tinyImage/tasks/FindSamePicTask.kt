package com.knight.transform.tinyImage.tasks

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.tasks.MergeResources
import com.knight.transform.Utils.Log
import com.knight.transform.tinyImage.Context
import com.knight.transform.tinyImage.utils.ImageUtil
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileInputStream

open class FindSamePicTask : DefaultTask() {


    var variant: BaseVariant? = null
    lateinit var context: Context
    @OutputFile
    var outputMappingFile = newOutputFile()


    @TaskAction
    fun startComparePic() {
        val samePicFile = outputMappingFile.get().asFile

        FileUtils.touch(samePicFile)
        MergeResources::class
                .java.declaredMethods.firstOrNull { it.name == "computeResourceSetList" && it.parameterCount == 0 }
                ?.run {
                    isAccessible = true
                    (invoke(variant?.mergeResources) as? Iterable<*>)?.mapNotNull { resourceSet ->
                        resourceSet?.javaClass?.methods?.find { it.name == "getSourceFiles" && it.parameterCount == 0 }?.invoke(resourceSet) as? Iterable<File>
                    }?.flatten()
                }?.forEach {
                    searchTargetFiles(it)
                }
        context.comparePictureMap.forEach { t, u ->
            if (u.size > 1) {
                putSamePicture(u)
            }
        }
        var hasSamePic = false
        val stringBuffer = StringBuffer("You have Same Picture!!!! please check them ,\n the output file at ${samePicFile.path}")
        var totalSaveSize = 0L
        context.samePictureMap.forEach { t, u ->
            if (u.size > 1) {
                stringBuffer.append("\n").append(Log.MIDDLE_BORDER)
                for (i: Int in 0 until u.size) {
                    val file = u[i]
                    stringBuffer.append("\n")
                    stringBuffer.append(file.absoluteFile)
                    if (i != u.size - 1) {
                        Log.i("${file.name}'s size is : ${file.length()}")
                        totalSaveSize += file.length()
                    }
                }
                hasSamePic = true
            }
        }
        if (hasSamePic) {
            val sizeStr = if (totalSaveSize < (1024f * 1024f)) String.format("%.3fKB", totalSaveSize.toFloat() / (1024f))
            else String.format("%.3fM", totalSaveSize.toFloat() / (1024f * 1024f))
            Log.w(stringBuffer.append("\n \n If you delete duplicate images, you will save ${sizeStr}").toString())
        }
        samePicFile.writeText(stringBuffer.toString())
    }

    private fun putSamePicture(pictures: ArrayList<File>) {
        pictures.forEach { file ->
            val fis = FileInputStream(file)
            val md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis))
            IOUtils.closeQuietly(fis)
            context.samePictureMap[md5]?.let {
                it.add(file)
            } ?: context.samePictureMap.let {
                val list = ArrayList<File>()
                list.add(file)
                it.put(md5, list)
            }
        }
    }

    private fun searchTargetFiles(file: File) {
        if (file.isDirectory) {
            file.listFiles().forEach {
                if (it.isFile && !ImageUtil.isImage(it)) {
                    return@forEach
                }
                searchTargetFiles(it)
            }
        } else if (ImageUtil.isImage(file)) {
            processImage(context, file)
            context.allPictureList.add(file)
        }
    }

    private fun processImage(context: Context, imgFile: File) {
        if (ImageUtil.isPicture(imgFile)) {
            context.comparePictureMap[imgFile.length()]?.let {
                it.add(imgFile)
            } ?: context.comparePictureMap.let {
                val list = ArrayList<File>()
                list.add(imgFile)
                it.put(imgFile.length(), list)
            }

        }
    }


}