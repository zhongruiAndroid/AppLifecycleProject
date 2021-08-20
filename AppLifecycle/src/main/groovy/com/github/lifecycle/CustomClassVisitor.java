package com.github.lifecycle;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CustomClassVisitor extends ClassVisitor implements Opcodes{
    private String mClassName;
    public CustomClassVisitor(int api) {
        super(api);
    }
    public CustomClassVisitor(ClassVisitor classVisitor ) {
        super(Opcodes.ASM7, classVisitor);
    }
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.mClassName = name;
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
        System.out.println("LifecycleClassVisitor : visitMethod -----> "+name);
        if("com/zr/applifecycleproject/MainActivity".equals(this.mClassName)){
            if("onCreate".equals(name)){
                MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
                CustomAdviceAdapter testAdviceAdapter = new CustomAdviceAdapter(api, methodVisitor, access, name, descriptor);
                return testAdviceAdapter;
            }
        }else if("androidx/appcompat/app/AppCompatDialog".equals(this.mClassName)){
            if("onCreate".equals(name)){
                MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
                CustomAdviceAdapter testAdviceAdapter = new CustomAdviceAdapter(api, methodVisitor, access, name, descriptor);
                return testAdviceAdapter;
            }
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
