package com.github.lifecycle;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

public class CustomClassVisitor extends ClassVisitor implements Opcodes{
    private String mClassName;
    private boolean isAdd;
    public CustomClassVisitor(int api) {
        super(api);
    }
    public CustomClassVisitor(ClassVisitor classVisitor ) {
        super(Opcodes.ASM7, classVisitor);
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.mClassName = name;
        Logger.i("mClassName:"+mClassName);
        super.visit(version, access, name, signature, superName, interfaces);
    }
    @Override
    public void visitEnd() {
        super.visitEnd();
    }
    /**
     * 扫描类的方法进行调用
     * @param access 修饰符
     * @param name 方法名字
     * @param descriptor 方法签名
     * @param signature 泛型信息
     * @param exceptions 抛出的异常
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if(this.isAdd){
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
        Logger.i("LifecycleClassVisitor : visitMethod -----> "+name);
        if("com/github/applifecycle/AppLifecycleHelper".equals(this.mClassName)){
            if("onCreate".equals(name)){
                MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
                CustomAdviceAdapter testAdviceAdapter = new CustomAdviceAdapter(api, methodVisitor, access, name, descriptor);
                return testAdviceAdapter;
            }
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        Logger.i("visitAnnotation===="+descriptor);
        if(isAdd){
            if(ClassHelper.ANNOTATION_NAME.equals(descriptor)){
                ClassHelper.get().addClassName(mClassName);
            }
        }
        return super.visitAnnotation(descriptor, visible);
    }
}
