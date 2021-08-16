package com.github.lifecycle;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.gradle.api.Project;

import java.util.Set;

public class AppLifecycleTransform extends Transform {
    private Project project;
    public AppLifecycleTransform(Project project) {
        this.project = project;
    }
    @Override
    public String getName() {
        return "AppLifecycleTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }
}
