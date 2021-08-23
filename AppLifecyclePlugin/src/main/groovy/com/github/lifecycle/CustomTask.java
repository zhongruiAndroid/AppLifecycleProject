package com.github.lifecycle;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;


public interface CustomTask {
    ClassVisitor createClassVisitor(ClassWriter classWriter,boolean isCollectClassName);
}
