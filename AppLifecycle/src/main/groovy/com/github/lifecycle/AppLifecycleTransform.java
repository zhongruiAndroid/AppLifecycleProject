package com.github.lifecycle;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Status;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.utils.FileUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.gradle.api.Project;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class AppLifecycleTransform extends Transform {
    private CustomTask task;
    private Project project;

    public AppLifecycleTransform(Project project, CustomTask task) {
        this.project = project;
        this.task = task;
    }

    /**
     * transform的名称
     * transformClassesWithMyClassTransformForDebug 运行时的名字
     * transformClassesWith + getName() + For + Debug或Release
     *
     * @return String
     */
    @Override
    public String getName() {
        return "AppLifecycleTransform";
    }

    /**
     * 需要处理的数据类型，有两种枚举类型
     * CLASSES和RESOURCES，CLASSES代表处理的java的class文件，RESOURCES代表要处理java的资源
     *
     * @return
     */
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    /**
     * 指Transform要操作内容的范围，官方文档Scope有7种类型：
     * EXTERNAL_LIBRARIES        只有外部库
     * PROJECT                   只有项目内容
     * PROJECT_LOCAL_DEPS        只有项目的本地依赖(本地jar)
     * PROVIDED_ONLY             只提供本地或远程依赖项
     * SUB_PROJECTS              只有子项目。
     * SUB_PROJECTS_LOCAL_DEPS   只有子项目的本地依赖项(本地jar)。
     * TESTED_CODE               由当前变量(包括依赖项)测试的代码
     * <p>
     * Returns the scope(s) of the Transform. This indicates which scopes the transform consumes.
     */
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    /**
     * 指明当前Transform是否支持增量编译
     * If it does, then the TransformInput may contain a list of changed/removed/added files, unless
     * something else triggers a non incremental run.
     */
    @Override
    public boolean isIncremental() {
        return true;
    }

    /**
     * Transform中的核心方法
     * transformInvocation.getInputs() 中是传过来的输入流，其中有两种格式，一种是jar包格式一种是目录格式。
     * transformInvocation.getOutputProvider() 获取到输出目录，最后将修改的文件复制到输出目录，这一步必须做不然编译会报错
     *
     * @param transformInvocation
     * @throws TransformException
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);

        Collection<TransformInput> inputs = transformInvocation.getInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();

        if (!isIncremental()) {
            outputProvider.deleteAll();
        }

        for (TransformInput item : inputs) {
            Collection<DirectoryInput> directoryInputs = item.getDirectoryInputs();
            if (directoryInputs == null) {
                continue;
            }
            for (DirectoryInput dirItem : directoryInputs) {
                handleDirectoryInput(dirItem, outputProvider, transformInvocation);
            }
            Collection<JarInput> jarInputs = item.getJarInputs();
            for (JarInput jarItem : jarInputs) {
                handleJarInputs(jarItem, outputProvider);
            }
        }
    }

    private void handleDirectoryInput(DirectoryInput dirItem, TransformOutputProvider outputProvider, TransformInvocation transformInvocation) {
        if (dirItem == null) {
            return;
        }
        //dirItem.getName():7a70a54cbab8dbb99af0592f6da21466925e759a
        //dirItem.getFile():E:\mystudy\my\StudyGradle\app\build\intermediates\javac\debug\classes
        File file = dirItem.getFile();
        Logger.i("handleDirectoryInput:DirectoryInput.getFile().getAbsolutePath():" + file.getAbsolutePath());
        //contentLocation.getAbsolutePath():类似于这种目录：E:\mystudy\my\StudyGradle\app\build\intermediates\transforms\DemoTransform\debug\33
        File contentLocation = outputProvider.getContentLocation(dirItem.getName(), dirItem.getContentTypes(), dirItem.getScopes(), Format.DIRECTORY);
        Logger.i("handleDirectoryInput:contentLocation.getAbsolutePath():" + contentLocation.getAbsolutePath());
        if (isIncremental()) {
            Map<File, Status> changedFiles = dirItem.getChangedFiles();
            if (changedFiles == null || changedFiles.size() == 0) {
                return;
            }
            for (Map.Entry<File, Status> item : changedFiles.entrySet()) {
                if (item == null) {
                    continue;
                }
                File changFile = item.getKey();
                Status value = item.getValue();
                if (value == Status.NOTCHANGED) {
                    Logger.i("handleDirectoryInput:not_changed.getAbsolutePath():" + changFile.getAbsolutePath());
                    continue;
                }
                File changFileResult = new File(contentLocation, changFile.getAbsolutePath().substring(file.getAbsolutePath().length()));
                switch (value) {
                    case ADDED:
                        Logger.i("handleDirectoryInput:added.getAbsolutePath():" + changFile.getAbsolutePath());
                    case CHANGED:
                        Logger.i("handleDirectoryInput:changed.getAbsolutePath():" + changFile.getAbsolutePath());
                        if (changFile.isFile()) {
                            handleFile(contentLocation, dirItem, changFile, outputProvider);
                        } else {
                            traverseDir(contentLocation, dirItem, changFile, outputProvider);
                        }
                        break;
                    case REMOVED:
                        Logger.i("handleDirectoryInput:removed.getAbsolutePath():" + changFile.getAbsolutePath());
                        if (changFileResult.isFile()) {
                            changFileResult.delete();
                        } else {
                            FileTools.deleteDir(changFileResult);
                        }
                        break;
                }
            }
        } else {
            try {
                FileUtils.copyDirectory(file, contentLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file.isDirectory()) {
                traverseDir(contentLocation, dirItem, file, outputProvider);
            } else {
                handleFile(contentLocation, dirItem, file, outputProvider);
            }
        }
    }

    private void traverseDir(File contentLocation, DirectoryInput dirItem, File fileDir, TransformOutputProvider outputProvider) {
        if (fileDir == null) {
            return;
        }
        File[] files = fileDir.listFiles();
        if (files == null) {
            return;
        }
        for (File fileItem : files) {
            if (fileItem == null) {
                continue;
            }
            if (fileItem.isFile()) {
                handleFile(contentLocation, dirItem, fileItem, outputProvider);
            } else {
                traverseDir(contentLocation, dirItem, fileItem, outputProvider);
            }
        }
    }

    private void handleFile(File contentLocation, DirectoryInput rootPath, File file, TransformOutputProvider outputProvider) {
        Logger.i("isIncremental:" + isIncremental() + ",handleFile:getAbsolutePath:" + file.getAbsolutePath());
        try {
            ClassReader classReader = new ClassReader(new FileInputStream(file));
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
            ClassVisitor visitor = task.createClassVisitor(classWriter);
            classReader.accept(visitor, ClassReader.EXPAND_FRAMES);
            byte[] bytes = classWriter.toByteArray();
            File changFileResult = new File(contentLocation, file.getAbsolutePath().substring(rootPath.getFile().getAbsolutePath().length()));
            FileOutputStream fos = new FileOutputStream(changFileResult);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.i("handleFile:IOException:" + e.getMessage());
        }
    }

    private void handleJarInputs(JarInput jarInput, TransformOutputProvider outputProvider) {
        if (jarInput == null) {
            return;
        }
        //jar本地路径，比如：C:\Users\Administrator\.gradle\caches\transforms-2\files-2.1\d07a998de6a8d24b66433d9fc1906c81\constraintlayout-1.1.3-runtime.jar
        File jarInputFile = jarInput.getFile();
        if (jarInputFile == null) {
            return;
        }
        String jarInputFileAbsolutePath = jarInputFile.getAbsolutePath();
        if (!jarInputFileAbsolutePath.toLowerCase().endsWith(".jar")) {
            return;
        }

        String md5Name = DigestUtils.md5Hex(jarInputFile.getAbsolutePath());
        //jar名字，比如：androidx.constraintlayout:constraintlayout:1.1.3
        String jarName = jarInput.getName();
        if (jarName.toLowerCase().endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length() - 4);
        }
        //contentLocation.getAbsolutePath()类似于这种具体的文件名：E:\mystudy\my\StudyGradle\app\build\intermediates\transforms\DemoTransform\debug\0.jar
        File contentLocation = outputProvider.getContentLocation(jarName + md5Name, jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR);
        if (isIncremental()) {
            Status status = jarInput.getStatus();

            switch (status) {
                case ADDED:
                case CHANGED:
                    handleJar(jarInputFile, jarName, contentLocation);
                    break;
                case REMOVED:
                    contentLocation.delete();
                    break;
            }
        } else {
            handleJar(jarInputFile, jarName, contentLocation);
        }

    }

    private void handleJar(File jarInputFile, String jarName, File contentLocation) {
        try {
            JarFile jarFile = new JarFile(jarInputFile);
            File tmpFile = new File(jarInputFile.getParent() + File.separator + "classes_temp.jar");
            //避免上次的缓存被重复插入
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile));
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String jarEntryName = jarEntry.getName();
                Logger.i("handleJar:jarEntryName" + jarEntryName);
                ZipEntry zipEntry = new ZipEntry(jarEntryName);
                if (zipEntry.isDirectory()) {
                    continue;
                }
                if (!jarEntryName.endsWith(".class")) {
                    continue;
                }
                InputStream inputStream = jarFile.getInputStream(zipEntry);
                jarOutputStream.putNextEntry(zipEntry);

                ClassReader classReader = new ClassReader(inputStream);//IOUtils.toByteArray(inputStream)
                ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
                ClassVisitor cv = task.createClassVisitor(classWriter);
                classReader.accept(cv, ClassReader.EXPAND_FRAMES);
                byte[] code = classWriter.toByteArray();

                jarOutputStream.write(code);

                jarOutputStream.closeEntry();
            }

            jarOutputStream.flush();
            jarOutputStream.close();
            jarFile.close();


            FileUtils.copyFile(tmpFile, contentLocation);
            tmpFile.delete();
        } catch (IOException e) {
            Logger.i("handleJar:IOException" + e.getMessage());
            e.printStackTrace();
        }
    }
}
