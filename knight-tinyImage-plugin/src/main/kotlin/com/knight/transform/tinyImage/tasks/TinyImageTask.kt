package com.knight.transform.tinyImage.tasks

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.tasks.MergeResources
import com.android.ide.common.res2.ResourceSet
import com.knight.transform.Utils.Log
import com.knight.transform.tinyImage.Context
import com.knight.transform.tinyImage.utils.CompressUtil
import com.knight.transform.tinyImage.utils.ImageUtil
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.TaskAction
import java.io.File

open class TinyImageTask : DefaultTask() {

    var variant: BaseVariant? = null
    var context: Context? = null
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
                    printFiles(it)
                }

    }

    fun printFiles(file: File) {
        if (file.isDirectory) {
            Log.i("liyachao==== ", "file directory name ::${file.name}")

            file.listFiles().forEach {
                if (it.isFile && !ImageUtil.isImage(it)) {
                    return@forEach
                }
                printFiles(it)
            }
        } else {
            originPictureTotalSize += file.length()
            CompressUtil.compressImg(context?.extension?.executeFileDir ?: "", file)
            afterCompressTotalSize += file.length()
            Log.i("liyachao==== ", "~~~~ origin size: ${originPictureTotalSize} \n after size: ${afterCompressTotalSize}")
        }
    }


}