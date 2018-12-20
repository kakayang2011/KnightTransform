package transform

import com.android.build.api.transform.*
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.internal.LoggerWrapper
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.ide.common.internal.WaitableExecutor
import transform.asm.BaseWeaver
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import java.io.File

open abstract class KnightTransform(project: Project) : Transform() {
    private val logger = LoggerWrapper.getLogger(javaClass::class.java)

    private val waitableExecutor = WaitableExecutor.useGlobalSharedThreadPool()

    protected lateinit var baseWeaver: BaseWeaver

    protected val isLibrary = project.plugins.hasPlugin(LibraryPlugin::class.java)

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
        return if (isLibrary) TransformManager.PROJECT_ONLY else TransformManager.SCOPE_FULL_PROJECT
    }

    open fun isNeedScanJar(): Boolean {
        return true
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
                                transformJar(jarInput.file, dest, isNeedScanJar())
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
                        transformJar(jarInput.file, dest, isNeedScanJar())
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

    private fun transformJar(srcJar: File, destJar: File, isNeedScan: Boolean) {
        waitableExecutor.execute {
            if (isNeedScan) {
                baseWeaver.weavJar(srcJar, destJar)
            } else {
                FileUtils.copyFile(srcJar, destJar)
            }
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