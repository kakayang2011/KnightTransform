package com.knight.transform.tinyImage.tasks

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.tasks.MergeResources
import com.android.ide.common.res2.ResourceSet
import com.knight.transform.Utils.Log
import com.knight.transform.tinyImage.Context
import com.knight.transform.tinyImage.utils.ImageUtil
import org.gradle.api.DefaultTask
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
        } else {
            originPictureTotalSize += file.length()
            ImageUtil.processImage(context, file)
            afterCompressTotalSize += file.length()
        }
    }


}