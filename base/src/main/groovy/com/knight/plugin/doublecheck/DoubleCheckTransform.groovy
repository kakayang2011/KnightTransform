package com.knight.plugin.doublecheck

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Status
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.ide.common.internal.WaitableExecutor
import com.google.common.io.Files
import com.knight.plugin.doublecheck.java.WeavedClass
import groovy.util.logging.Slf4j
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

import static com.google.common.base.Preconditions.checkNotNull

@Slf4j
class DoubleCheckTransform extends Transform {
    private Map<String, List<WeavedClass>> weavedVariantClassesMap

    DoubleCheckTransform(Map<String, List<WeavedClass>> weavedVariantClassesMap) {
        this.weavedVariantClassesMap = weavedVariantClassesMap
    }

    @Override
    String getName() {
        return "DoubleCheckTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation invocation) throws TransformException, InterruptedException, IOException {
        println("=====now,enter the transform==== ${invocation.isIncremental()}")
        def weavedClassesContainer = []
        weavedVariantClassesMap[invocation.context.variantName] = weavedClassesContainer

        TransformOutputProvider outputProvider = checkNotNull(invocation.getOutputProvider(),
                "Missing output object for transform " + getName())
        //如果非增量，则清空旧的输出内容
        if (!invocation.isIncremental()) outputProvider.deleteAll()
        WaitableExecutor waitableExecutor = WaitableExecutor.useGlobalSharedThreadPool()

        invocation.inputs.each { inputs ->
            inputs.jarInputs.each { JarInput jarInput ->
                File inputJar = jarInput.file
                def jarName = jarInput.name
                println("=====jar = " + jarInput.file.getAbsolutePath())
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                File outputJar = invocation.outputProvider.getContentLocation(
                        jarName + md5Name,
                        jarInput.contentTypes,
                        jarInput.scopes,
                        Format.JAR)
                println("=====input jar = ${inputJar.name}")
                println("=====output jar = ${outputJar.name}")
//                waitableExecutor.execute {
//                    FileUtils.copyFile(jarInput.file, outputJar)
//                    return null
//                }
                if (DoubleCheckConfig.isScanJar) {
                    if (invocation.isIncremental()) {
                        def status = jarInput.status
                        if (status != Status.NOTCHANGED) {
                            println("changed jar = ${jarInput.name}:${status}")
                        }
                        switch (status) {
                            case Status.NOTCHANGED:
                                break
                            case Status.ADDED:
                            case Status.CHANGED:
                                waitableExecutor.execute {
                                    DoubleCheckInject.injectJar(inputJar, outputJar, weavedClassesContainer)
                                    return null
                                }
                                break
                            case Status.REMOVED:
                                FileUtils.forceDelete(outputJar)
                                break
                        }
                    } else {
                        waitableExecutor.execute {
                            DoubleCheckInject.injectJar(inputJar, outputJar, weavedClassesContainer)
                            return null
                        }
                    }
                }
            }

            inputs.directoryInputs.each { DirectoryInput directoryInput ->
                File inputDir = directoryInput.file
                File outputDir = invocation.outputProvider.getContentLocation(
                        directoryInput.name,
                        directoryInput.contentTypes,
                        directoryInput.scopes,
                        Format.DIRECTORY)
//                waitableExecutor.execute {
//                FileUtils.copyDirectory(directoryInput.file, outputDir)
//                    return null
//                }
                FileUtils.forceMkdir(outputDir)
                if (invocation.isIncremental()) {
                    directoryInput.changedFiles.each { File inputFile, Status status ->
                        if (status != Status.NOTCHANGED) {
                            println("changed jar = ${inputFile.name}:${status}")
                        }
                        switch (status) {
                            case Status.NOTCHANGED:
                                break
                            case Status.ADDED:
                            case Status.CHANGED:
                                if (!inputFile.isDirectory() && Utils.isMatchCondition(inputFile.name)) {
                                    waitableExecutor.execute {
                                        File outputFile = Utils.toOutputFile(outputDir, inputDir, inputFile)
                                        DoubleCheckInject.injectFile(inputFile, outputFile, weavedClassesContainer)
                                        return null
                                    }
                                }
                                break
                            case Status.REMOVED:
                                waitableExecutor.execute {
                                    if (inputFile.exists()) {
                                        //noinspection ResultOfMethodCallIgnored
                                        inputFile.delete()
                                    }

                                    return null
                                }
                                break
                        }
                    }
                } else {
                    waitableExecutor.execute {
                        for (File inputFile : com.android.utils.FileUtils.getAllFiles(inputDir)) {

                            File outputFile = Utils.toOutputFile(outputDir, inputDir, inputFile)
                            if (Utils.isMatchCondition(inputFile.name)) {
                                DoubleCheckInject.injectFile(inputFile, outputFile, weavedClassesContainer)
                            } else {
                                if (inputFile.isFile()) {
                                    Files.createParentDirs(outputFile)
                                    FileUtils.copyFile(inputFile, outputFile)
                                }
                            }
                            return null
                        }
                    }
                }
            }
        }
        waitableExecutor.waitForTasksWithQuickFail(true)

    }
}
