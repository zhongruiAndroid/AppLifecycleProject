package com.github.lifecycle;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassHelper {
    public static final String ANNOTATION_NAME = "Lcom/github/applifecycle/ApplicationLifecycle;";
    /**********************************************************/
    private static ClassHelper singleObj;

    private ClassHelper() {
    }

    public static ClassHelper get() {
        if (singleObj == null) {
            synchronized (ClassHelper.class) {
                if (singleObj == null) {
                    singleObj = new ClassHelper();
                }
            }
        }
        return singleObj;
    }

    private List<String> list = new ArrayList<>();

    /**********************************************************/
    public void addClassPath(String classPath) {
        if (classPath == null || classPath.length() == 0) {
            return;
        }
        Logger.i("addClassPath:" + classPath);
        //com/example/test/MainActivity.class
        String replace = classPath.replace(File.separator, ".").replace(".class", "");
        if(list.size()>0&&list.contains(replace)){
            return;
        }
        list.add(replace);
    }
    public void addClassName(String classPath) {
        if (classPath == null || classPath.length() == 0) {
            return;
        }
        Logger.i("addClassName:" + classPath);
        //com.example.test.MainActivity
        if(list.size()>0&&list.contains(classPath)){
            return;
        }
        list.add(classPath);
    }

    public List<String> getList() {
        return list;
    }
    public void clear(){
        if (list != null) {
            list.clear();
        }
    }
}
