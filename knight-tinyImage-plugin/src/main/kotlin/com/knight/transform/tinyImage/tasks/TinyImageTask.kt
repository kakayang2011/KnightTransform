package com.knight.transform.tinyImage.tasks

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.tasks.MergeResources
import com.android.ide.common.res2.ResourceSet
import com.knight.transform.Utils.Log
import com.knight.transform.tinyImage.Context
import com.knight.transform.tinyImage.utils.ImageUtil
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import java.io.File

open class TinyImageTask : DefaultTask() {

    companion object {
        val TAG = "TinyImageTask"
    }

    var variant: BaseVariant? = null
    lateinit var context: Context
    var originPictureTotalSize = 0L
    var afterCompressTotalSize = 0L


    @TaskAction
    fun startCompressPic() {
        MergeResources::class
                .java.declaredMethods.firstOrNull { it.name == "computeResourceSetList" && it.parameterCount == 0 }
                ?.run {
                    isAccessible = true
                    (invoke(variant?.mergeResources) as? List<ResourceSet>)?.mapNotNull {
                        it.sourceFiles
                    }?.flatten()
                }?.forEach {
                    searchTargetFiles(it)
                }
        context.bigImageList.let {
            if (it.isNotEmpty()) {
                val stringBuffer = StringBuffer("You have big Img!!!! please check them \n")
                for (i: Int in 0 until it.size) {
                    stringBuffer.append(it[i].absoluteFile)
                    stringBuffer.append("\n")
                }
                throw GradleException(stringBuffer.toString())
            }
        }
        Log.i(TAG, "picture(png jpeg web) origin size: ${originPictureTotalSize / 1024}KB =====> after size: ${afterCompressTotalSize / 1024}KB , shrink ${(((originPictureTotalSize - afterCompressTotalSize).toFloat() / (originPictureTotalSize)) * 100).toInt()} percentage")
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
            val oldFileSize = file.length()
            originPictureTotalSize += oldFileSize
            val newFileSize = processImage(context, file)
            afterCompressTotalSize += newFileSize

            if (newFileSize < oldFileSize) {
                context.compressPngList.add(file)
            }
        }
    }

    private fun processImage(context: Context, imgFile: File): Long {
        imgFile.takeIf {
            it.length() >= context.extension.maxSize
        }?.let {
            context.bigImageList.add(it)
            return it.length()
        } ?: imgFile.takeIf {
            !context.extension.whiteList.contains(it.name) && context.extension.webp && (ImageUtil.isSupportConvertToWebpWithAlpha(context.project)
                    || (ImageUtil.isSupportConvertToWebp(context.project) && !ImageUtil.isAlphaPNG(it)))
        }?.let {
            val webpFile = File("${imgFile.path.substring(0, imgFile.path.lastIndexOf("."))}.webp")
            ImageUtil.command(context, "cwebp ${imgFile.path} -o ${webpFile.path} -quiet")
            if (webpFile.length() < imgFile.length()) {
                if (imgFile.exists()) {
                    imgFile.delete()
                }
                context.convertWebpList.add(webpFile)
                return webpFile.length()
            } else {
                //如果webp的大的话就抛弃
                if (webpFile.exists()) {
                    webpFile.delete()
                }
            }
            return it.length()
        } ?: imgFile.takeIf {
            !context.extension.whiteList.contains(it.name) && context.extension.compress
        }?.let {
            if (ImageUtil.isJPG(imgFile)) {
                ImageUtil.command(context, "guetzli ${imgFile.path} ${imgFile.path}")
            } else {
                ImageUtil.command(context, "pngquant --skip-if-larger --speed 3 --force --output ${imgFile.path} -- ${imgFile.path}")
            }
        }
        return imgFile.length()
    }


}