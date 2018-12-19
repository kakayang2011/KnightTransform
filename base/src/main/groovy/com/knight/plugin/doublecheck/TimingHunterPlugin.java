package com.knight.plugin.doublecheck;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Collections;

/**
 * Created by Quinn on 25/02/2017.
 */
public class TimingHunterPlugin implements Plugin<Project> {

    @SuppressWarnings("NullableProblems")
    @Override
    public void apply(Project project) {
        AppExtension appExtension = (AppExtension) project.getProperties().get("android");
        appExtension.registerTransform(new DoubleCheckTransformj(null), Collections.EMPTY_LIST);
    }

}
