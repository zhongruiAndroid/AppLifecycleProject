package com.github.lifecycle

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class AppLifecyclePlugin implements Plugin<Project> {
    void apply(Project project) {
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new AppLifecycleTransform(project, new CustomTask() {
            @Override
            ClassVisitor createClassVisitor(ClassWriter classWriter, boolean isCollectClassName) {
                CustomClassVisitor classVisitor = new CustomClassVisitor(classWriter)
                classVisitor.setAdd(isCollectClassName)
                return classVisitor;
            }
        }))
    }
}