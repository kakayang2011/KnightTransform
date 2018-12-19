package transform

import com.android.build.api.transform.*
import com.android.build.gradle.internal.LoggerWrapper
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.ide.common.internal.WaitableExecutor
import transform.asm.BaseWeaver
import org.apache.commons.io.FileUtils
import java.io.File

open class KnightTransform : Transform() {
    private val logger = LoggerWrapper.getLogger(KnightTransform::class.java)

    private val waitableExecutor = WaitableExecutor.useGlobalSharedThreadPool()

    protected lateinit var baseWeaver: BaseWeaver

    override fun getName(): String {
        return this.javaClass.simpleName
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun isIncremental(): Boolean {
        return true
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }


    override fun transform(transform: TransformInvocation) {
        val startTime = System.currentTimeMillis()

        transform.apply {
            if (!isIncremental) outputProvider.deleteAll()
            inputs.forEach {
                it.jarInputs.forEach { jarInput ->
                    val status = jarInput.status
                    val dest = outputProvider.getContentLocation(
                            jarInput.file.absolutePath,
                            jarInput.contentTypes,
                            jarInput.scopes,
                            Format.JAR)
                    if (isIncremental) {
                        when (status) {
                            Status.ADDED, Status.CHANGED -> {
                                transformJar(jarInput.file, dest)
                            }
                            Status.REMOVED -> {
                                if (dest.exists()) {
                                    FileUtils.forceDelete(dest)
                                }
                            }
                            else -> {
                            }
                        }
                    } else {
                        transformJar(jarInput.file, dest)
                    }
                }

                it.directoryInputs.forEach { directoryInput ->
                    val dest = outputProvider.getContentLocation(
                            directoryInput.name,
                            directoryInput.contentTypes,
                            directoryInput.scopes,
                            Format.DIRECTORY)
                    FileUtils.forceMkdir(dest)
                    if (isIncremental) {
                        val srcDirPath = directoryInput.file.absolutePath
                        val destDirPath = dest.absolutePath
                        directoryInput.changedFiles.forEach { inputFile, status ->
                            val destFilePath = inputFile.absolutePath.replace(srcDirPath, destDirPath)
                            val destFile = File(destFilePath)
                            when (status) {
                                Status.ADDED, Status.CHANGED -> {
                                    FileUtils.touch(destFile)
                                    transformSingleFile(inputFile, destFile, srcDirPath)
                                }
                                Status.REMOVED -> {
                                    if (destFile.exists()) {
                                        destFile.delete()
                                    }
                                }
                                else -> {
                                }
                            }
                        }
                    } else {
                        transformDir(directoryInput.file, dest)
                    }

                }
            }
        }
        waitableExecutor.waitForTasksWithQuickFail<Any>(true)
        val costTime = System.currentTimeMillis() - startTime
        logger.info(name + " costed " + costTime + "ms")
        println(name + " costed " + costTime + "ms")
    }

    private fun transformJar(srcJar: File, destJar: File) {
        waitableExecutor.execute {
            baseWeaver.weavJar(srcJar, destJar)
        }
    }

    private fun transformSingleFile(inputFile: File, outputFile: File, srcDir: String) {
        waitableExecutor.execute {
            baseWeaver.weaveFile(inputFile, outputFile, srcDir)
        }
    }

    private fun transformDir(inputFile: File, outputFile: File) {
        val inputDirPath = inputFile.getAbsolutePath()
        val outputDirPath = outputFile.getAbsolutePath()
        if (inputFile.isDirectory) {
            com.android.utils.FileUtils.getAllFiles(inputFile).forEach {
                waitableExecutor.execute {
                    val filePath = it.absolutePath
                    val outputFile = File(filePath.replace(inputDirPath, outputDirPath))
                    baseWeaver.weaveFile(it, outputFile, inputDirPath)
                }
            }
        }
    }

}