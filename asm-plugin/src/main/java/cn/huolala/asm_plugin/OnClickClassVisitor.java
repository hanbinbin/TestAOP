package cn.huolala.asm_plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 定义一个ClassVisitor，用来访问所有的java类
 */
public class OnClickClassVisitor extends ClassVisitor implements Opcodes {
    private String className = "android/view/View$OnClickListener";
    private String methodName = "onClick";
    private String methodDesc = "(Landroid/view/View;)V";
    private boolean isMatchClass = false;

    public OnClickClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM9, classVisitor);
    }

    /**
     * @param version    class文件的jdk版本，如50代表JDK 1.7版本
     * @param access     类的修饰符，如public、final等
     * @param name       类名，但是会以路径的形式来表示，如cn.huolala.mytestapplication.BlankFragment这个类，
     *                   最后visit方法中的类名为cn/huolala/mytestapplication/BlankFragment
     * @param signature  泛型信息，如果未定义泛型，则该参数为null
     * @param superName  父类名
     * @param interfaces 该类实现的接口列表
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        isMatchClass = isMatchClass(interfaces, className);
    }

    private boolean isMatchClass(String[] interfaces, String className) {
        for (String anInterface : interfaces) {
            if (anInterface.equals(className)) {
                return true;
            }
        }
        return isMatchClass;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        System.out.println("visitMethod");
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        // 如果该类实现类点击事件接口，且该方法是onClick，方法参数是 (Landroid/view/View;)V  ;就在该方法中进行代码织入
        if (isMatchClass && methodName.equals(name) && methodDesc.equals(descriptor)) {
            return new OnClickMethodVisitor(mv);
        }
        return mv;
    }

    /**
     * 类访问结束的时候调用
     */
    @Override
    public void visitEnd() {

//        // 如果该类是Fragment子类，且没有重写onResume方法，那么就添加一个onResume方法
//        if (mIsFragmentClass && !mHasResumeMethod) {
//            // 生成方法 public void onResume()
//            GeneratorAdapter generator = new GeneratorAdapter(ACC_PUBLIC, new Method("onResume", "()V"), null, null, cv);
//            // 将this对象压入操作栈中，这里其实就是这个Fragment对象
//            generator.loadThis();
//            // 调用 super.onResume()
//            generator.invokeConstructor(Type.getObjectType("android/app/Fragment"), new Method("onResume", "()V"));
//
//            // 将this对象压入操作栈中，这里其实就是这个Fragment对象
//            generator.loadThis();
//            // 调用静态方法 com.growingio.asmdemo.FragmentAsmInjector#afterFragmentResume(Fragment fragment)
//            generator.invokeStatic(Type.getObjectType("com/growingio/asmdemo/FragmentAsmInjector"), new Method("afterFragmentResume", "(Landroid/app/Fragment;)V"));
//
//            // 调用return并结束该方法
//            generator.returnValue();
//            generator.endMethod();
//        }
        super.visitEnd();
    }
}