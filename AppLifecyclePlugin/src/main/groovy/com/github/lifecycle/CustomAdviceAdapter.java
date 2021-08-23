package com.github.lifecycle;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.List;

public class CustomAdviceAdapter extends AdviceAdapter {
    protected CustomAdviceAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
    }
    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        List<String> list = ClassHelper.get().getList();
        if(list==null){
            return;
        }
        for (String item:list){
//            mv.visitLdcInsn(item);
//            mv.visitMethodInsn(INVOKEVIRTUAL, "com/github/applifecycle/AppLifecycleHelper", "addAppLifecycle", "(Ljava/lang/String;)V", false);
        }
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
    }
}
