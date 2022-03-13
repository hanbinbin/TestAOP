package cn.huolala.asm_plugin.transform;

import com.android.build.api.transform.*;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

import cn.huolala.asm_plugin.OnClickClassVisitor;

import org.apache.commons.compress.utils.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2/27/22.
 * PS: Not easy to write code, please indicate.
 */
public class ASMTransform extends Transform {

    // transform 名字
    @Override
    public String getName() {
        return "ASMTransform";
    }


    /**
     * 输入类型
     * - QualifiedContent.DefaultContentType.RESOURCES
     * 资源文件
     * - QualifiedContent.DefaultContentType.CLASSES
     * class文件
     *
     * @return
     */
    // 输入文件的类型
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        // 只处理java class文件
        return TransformManager.CONTENT_CLASS;
    }

    /**
     * 作用范围
     * <p>
     * - QualifiedContent.Scope.PROJECT
     * 只作用于project本身内容
     * - QualifiedContent.Scope.SUB_PROJECTS
     * 子模块内容
     * - QualifiedContent.Scope.EXTERNAL_LIBRARIES
     * 只包含外部库
     * - QualifiedContent.Scope.TESTED_CODE
     * 当前变体测试的代码以及包括测试的依赖项
     * - QualifiedContent.Scope.PROVIDED_ONLY
     * 支持compileOnly的远程依赖
     *
     * @return
     */
    // 指定作用范围
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        // 处理整个project，包括依赖
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    // 是否支持增量
    @Override
    public boolean isIncremental() {
        return false;
    }

    /**
     * TransformInvocation
     * <p>
     * public interface TransformInvocation {
     *
     * @param transformInvocation
     * @throws TransformException
     * @throws InterruptedException
     * @throws IOException
     * @NonNull Context getContext();
     * <p>
     * // 返回transform的输入源
     * @NonNull Collection<TransformInput> getInputs();
     * <p>
     * // 返回引用型输入源
     * @NonNull Collection<TransformInput> getReferencedInputs();
     * <p>
     * // 额外输入源
     * @NonNull Collection<SecondaryInput> getSecondaryInputs();
     * <p>
     * // 输出源
     * @Nullable TransformOutputProvider getOutputProvider();
     * <p>
     * boolean isIncremental();
     * }
     * <p>
     * 字节码文件打开是一堆十六进制数
     * <p>
     * 查看字节码
     * 通过 javap
     * <p>
     * 访问者模式
     * 访问者模式主要用于修改或操作一些数据结构比较稳定的数据，而字节码文件的结构是由JVM固定的，所以很适合利用访问者模式对字节码文件进行修改。
     * <p>
     * - ClassReader：用于读取已经编译好的.class文件。现有Java类的解析器。这个类主要解析符合Java类文件格式的字节码，在遇到每个属性、
     * 每个方法和字节码指令调用的时候调用相应的访问方法。
     * <p>
     * - ClassWriter：用于重新构建编译后的类，如修改类名、属性以及方法，也可以生成新的类的字节码文件。以字节码的形式生成Java类的类访问器。
     * 更准确地说，这个类可以生成一个符合Java类文件格式的字节码。它可以单独使用，“从零开始”生成Java类，
     * 也可以与一个或多个ClassReader一起使用，从一个或多个现有Java类生成修改后的类。
     * <p>
     * - 各种Visitor类：CoreAPI根据字节码从上到下依次处理，对于字节码文件中不同的区域有不同的Visitor，
     * 比如用于访问方法的MethodVisitor、用于访问类变量的FieldVisitor、用于访问注解的AnnotationVisitor等
     * <p>
     * ClassVisitor
     * 访问Java类的访问器。主要负责解析Java类的注解、各种方法、各种属性等。
     * MethodVisitor
     * 访问Java方法的访问器，主要负责解析和生成Java的方法。
     * GeneratorAdapter
     * MethodVisitor的子类，封装了一些常用方法，用来方便的生成Java方法。
     * AdviceAdapter
     * GeneratorAdapter的子类，和GeneratorAdapter不同的是，他是一个抽象类，需要用户继承并重写。在方法访问之前、之后等时机执行相应的访问方法。
     * <p>
     * 作者：小肥阳
     * 链接：https://juejin.cn/post/6888555395074555918
     * 来源：稀土掘金
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * <p>
     * eg:
     * public static void main(String[] args) throws Exception {
     * //读取
     * ClassReader classReader = new ClassReader("XXX");
     * ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
     * //处理
     * ClassVisitor classVisitor = new CustomClassVisitor(classWriter);
     * classReader.accept(classVisitor, ClassReader.SKIP_DEBUG);
     * byte[] data = classWriter.toByteArray();
     * //输出
     * File f = new File("XXX.class");
     * FileOutputStream fout = new FileOutputStream(f);
     * fout.write(data);
     * fout.close();
     * }
     */
    // transform执行函数
    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        if (outputProvider == null) {
            return;
        }
        outputProvider.deleteAll();
        // 遍历所有输入文件
        for (TransformInput transformInput : inputs) {
            // 遍历所有文件夹
            for (DirectoryInput directoryInput : transformInput.getDirectoryInputs()) {
                handleDirectoryInput(directoryInput, outputProvider);
            }
            // 遍历所有jar包
            for (JarInput jarInput : transformInput.getJarInputs()) {
                handleJarInput(jarInput, outputProvider);
            }
        }
    }

    void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        File inputDirectory = directoryInput.getFile();
        // 获取文件输出的目标文件夹
        File outputDirectory = outputProvider.getContentLocation(
                directoryInput.getName(),
                directoryInput.getContentTypes(),
                directoryInput.getScopes(),
                Format.DIRECTORY);

        if (inputDirectory.isDirectory()) {
            // 遍历文件夹下面的所有文件
            FileUtils.getAllFiles(inputDirectory).forEach(srcFile -> {
                String relativePath = FileUtils.relativePath(srcFile, inputDirectory);//计算相对路径
                File dstFile = FileUtils.join(outputDirectory, relativePath);
                FileUtils.mkdirs(dstFile.getParentFile());
                // 文件时class文件
                if (srcFile.getName().endsWith(".class")) {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(srcFile);
                        ClassReader classReader = new ClassReader(fileInputStream);
                        //1.ClassWriter.COMPUTE_MAXS :
                        //这种方式会自动计算上述操作数栈和局部变量表的大小 但需要手动触发
                        //通过调用org.objectweb.asm.commons.LocalVariablesSorter#visitMaxs
                        //触发参数可以随便写
                        //2.ClassWriter.COMPUTE_FRAMES
                        //不仅会计算上述操作数栈和局部变量表的大小 还会自动计算StackMapFrames
                        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
                        ClassVisitor classVisitor = new OnClickClassVisitor(classWriter);
                        // 1.ClassReader.EXPAND_FRAMES : 展开 StackMapTable 属性; 2.ClassReader.SKIP_DEBUG : 跳过类文件中的调试信息，比如行号(LineNumberTable)等
                        // 3.ClassReader.SKIP_CODE : 跳过方法体中的code属性 （方法字节码，异常表等等）4.ClassReader.SKIP_FRAMES : 跳过 StackMapTable 属性
                        classReader.accept(classVisitor, ClassReader.SKIP_FRAMES);
                        FileOutputStream fileOutputStream = new FileOutputStream(dstFile);
                        fileOutputStream.write(classWriter.toByteArray());
                        fileOutputStream.close();
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        FileUtils.copyFile(srcFile, dstFile); //非class文件直接拷贝到目标文件
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    void handleJarInput(JarInput jarInput, TransformOutputProvider outputProvider) {
        File inputJar = jarInput.getFile();
        // 获取jar包输出的目标文件
        File outputJar = outputProvider.getContentLocation(
                jarInput.getName(),
                jarInput.getContentTypes(),
                jarInput.getScopes(),
                Format.JAR
        );
        FileUtils.mkdirs(outputJar.getParentFile());
        transformJar(inputJar, outputJar);
    }

    void transformJar(File inputJar, File outputJar) {
        //如果对jar包文件不需要做修改的，直接将jar包拷贝到目标文件
//        FileUtils.copyFile(inputJar, outputJar);

        // 第一种方式遍历jar文件
//        try {
//            JarInputStream inputJarStream = new JarInputStream(new FileInputStream(inputJar));
//            JarOutputStream outputJarStream = new JarOutputStream(new FileOutputStream(outputJar));
//            while (true) {
//                //jar中的每一个文件夹/每一个文件 都是一个jarEntry
//                JarEntry inputJarEntry = inputJarStream.getNextJarEntry();
//                if (inputJarEntry == null) {
//                    break;
//                }
//                String entryName = inputJarEntry.getName();
//                JarEntry outputJarEntry = new JarEntry(entryName);
//                outputJarStream.putNextEntry(outputJarEntry);//表示将该entry写入jar文件中 也就是创建该文件夹和文件
//                if (!inputJarEntry.isDirectory() && entryName.endsWith(".class")) {
//                    ClassReader classReader = new ClassReader(inputJarStream);
//                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
//                    ClassVisitor classVisitor = new OnClickClassVisitor(classWriter);
//                    classReader.accept(classVisitor, ClassReader.SKIP_FRAMES);
//                    outputJarStream.write(classWriter.toByteArray());
//                } else {
//                    //第一种方式
//                    byte[] buffer = new byte[1024];
//                    int bytesRead;
//                    while ((bytesRead = inputJarStream.read(buffer)) != -1) {
//                        outputJarStream.write(buffer, 0, bytesRead);
//                    }
//                }
//            }
//            inputJarStream.close();
//            outputJarStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // 第二种方式遍历jar文件
        try {
            JarOutputStream outputJarStream = new JarOutputStream(new FileOutputStream(outputJar));
            JarFile originJar = new JarFile(inputJar);
            // 遍历原jar文件寻找class文件
            Enumeration<JarEntry> enumeration = originJar.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry originEntry = enumeration.nextElement();
                InputStream inputStream = originJar.getInputStream(originEntry);
                String entryName = originEntry.getName();
                if (entryName.endsWith(".class")) {
                    JarEntry destEntry = new JarEntry(entryName);
                    outputJarStream.putNextEntry(destEntry);
                    byte[] sourceBytes = IOUtils.toByteArray(inputStream);
                    // 修改class文件内容
                    byte[] modifiedBytes = modifyClass(sourceBytes);
                    if (modifiedBytes == null) {
                        modifiedBytes = sourceBytes;
                    }
                    outputJarStream.write(modifiedBytes);
                    outputJarStream.closeEntry();
                } else {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputJarStream.write(buffer, 0, bytesRead);
                    }
                }
                inputStream.close();
            }
            outputJarStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private byte[] modifyClass(byte[] classBytes) {
        ClassReader classReader = new ClassReader(classBytes);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new OnClickClassVisitor(classWriter);
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }

}
