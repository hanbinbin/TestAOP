package cn.huolala.apt_compiler;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import cn.huolala.apt_annotion.OnClick;

/**
 * 注解处理器 生成类代码
 * <p>
 * 那么Processor是什么呢，其实Processor会在编译阶段对代码进行扫描，然后获取到对应的注解，之后调用process方法，
 * 然后我们根据这些注解类来做一些后续操作。
 */
@AutoService(Processor.class)
public class APTProcessor extends AbstractProcessor {
    private Filer filer;
    private Elements elementUtils;
    public static Messager messager; //打印消息

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
    }

    /**
     * - getSupportedAnnotationTypes方法，注解处理器需要处理的注解
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(OnClick.class.getCanonicalName());
        return set;
    }

    /**
     * - process方法，注解处理器具体的处理方法
     * - RoundEnvironment对象的getElementsAnnotatedWith方法，可以拿到Element的集合
     * <p>
     * Element
     * Element接口有5个子接口
     * 1. ExecutableElement
     * 表示某个类或者接口的方法、构造方法或初始化程序（静态或者实例）
     * 对应ElementType.METHOD，ElementType.CONSTRUCTOR
     * <p>
     * 2. PackageElement
     * 表示一个包程序元素
     * 对应ElementType.PACKAGE
     * <p>
     * 3. TypeElement
     * 表示一个类或者接口程序元素
     * 对应ElementType.TYPE
     * <p>
     * 4. TypeParameterElement
     * 表示泛型类，接口，方法或构造函数元素的正式类型参数
     * 对应ElementType.PARAMETER
     * <p>
     * 5. VariableElement
     * 表示一个字段、enum 常量、方法或者构造方法的参数、局部变量或异常参数
     * 对应ElementType.LOCAL_VARIABLE
     * <p>
     * <p>
     * // 获取父元素
     * element.getEnclosingElement();
     * // 获取子元素
     * element.getEnclosedElements();
     * 注: 只有element和强转的类型一样时才能cast
     *
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "APTProcessor start");
        //获取封装信息
        Map<String, BinderInfo> binderInfoMap = generateInfoMap(roundEnv);
        //生成代码
        for (String key : binderInfoMap.keySet()) {
            BinderInfo binderInfo = binderInfoMap.get(key);
            writeCode(binderInfo);
        }
        messager.printMessage(Diagnostic.Kind.NOTE, "APTProcessor end");
        return true;
    }

    private Map<String, BinderInfo> generateInfoMap(RoundEnvironment roundEnv) {
        Map<String, BinderInfo> binderInfoMap = new HashMap<>();
        //遍历注解  getElementsAnnotatedWith返回用给定注解类型注解的元素
        for (Element element : roundEnv.getElementsAnnotatedWith(OnClick.class)) {
            //获取方法
            ExecutableElement executableElement = (ExecutableElement) element;
            //获取类
            TypeElement classElement = (TypeElement) executableElement.getEnclosingElement();
            //获取包名
            PackageElement packageElement = elementUtils.getPackageOf(element);

            String methodName = executableElement.getSimpleName().toString();
            String fullClassName = classElement.getQualifiedName().toString();
            String className = classElement.getSimpleName().toString();
            String packageName = packageElement.getQualifiedName().toString();

            messager.printMessage(Diagnostic.Kind.NOTE, methodName);
            messager.printMessage(Diagnostic.Kind.NOTE, className);
            messager.printMessage(Diagnostic.Kind.NOTE, fullClassName);
            messager.printMessage(Diagnostic.Kind.NOTE, packageName);

            //获取注解参数
            int viewId = executableElement.getAnnotation(OnClick.class).value();
            BinderInfo binderInfo = binderInfoMap.get(fullClassName);
            //如果当前类信息已经创建，则直接添加
            if (binderInfo != null) {
                OnClickInfo onClickInfo = new OnClickInfo(viewId, methodName);
                binderInfo.addMethod(onClickInfo);
            } else {
                binderInfo = new BinderInfo(packageName, fullClassName, className);
                binderInfo.setTypeElement(classElement);
                OnClickInfo onClickInfo = new OnClickInfo(viewId, methodName);
                binderInfo.addMethod(onClickInfo);
                binderInfoMap.put(fullClassName, binderInfo);
            }
        }
        return binderInfoMap;
    }

    private void writeCode(BinderInfo binderInfo) {
        try {
            JavaFileObject javaFileObject = filer.createSourceFile(binderInfo.getFullClassName() + "_Binder", binderInfo.getTypeElement());
            Writer writer = javaFileObject.openWriter();
            writer.write(binderInfo.generateJavaCode());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }
}