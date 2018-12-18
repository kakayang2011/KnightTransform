package com.knight.plugin.doublecheck

import com.android.SdkConstants
import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.FeatureExtension
import com.android.build.gradle.LibraryExtension
import com.android.utils.FileUtils

class Utils {

    /*'R\$.class' and 'BuildConfig.class' or some other class*/
    private static final List<String> EXCLUSIVECLASS = Collections.unmodifiableList(
            Arrays.asList("BuildConfig.class",
                    "R.class",
                    "R\$string.class",
                    "R\$attr.class",
                    "R\$anim.class",
                    "R\$bool.class",
                    "R\$color.class",
                    "R\$dimen.class",
                    "R\$drawable.class",
                    "R\$mipmap.class",
                    "R\$id.class",
                    "R\$integer.class",
                    "R\$layout.class",
                    "R\$string.class ",
                    "R\$style.class",
                    "R\$styleable.class",))

    static def path2Classname(String entryName) {
        entryName.replace(File.separator, ".").replace(".class", "")
    }

    static File toOutputFile(File outputDir, File inputDir, File inputFile) {
        return new File(outputDir, FileUtils.relativePossiblyNonExistingPath(inputFile, inputDir))
    }

    static boolean isMatchCondition(String name) {
        name.endsWith(SdkConstants.DOT_CLASS) && //
                !("${path2Classname(name).split("\\.").last()}.class".toString() in EXCLUSIVECLASS)
    }
}
