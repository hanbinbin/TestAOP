package cn.huolala.apt_compiler;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2/26/22.
 * PS: Not easy to write code, please indicate.
 */
public class BinderInfo {
    private String packageName;
    private String fullClassName;
    private String className;
    private TypeElement typeElement;
    private List<OnClickInfo> methods = new ArrayList<>();

    public BinderInfo(String packageName, String fullClassName, String className) {
        this.packageName = packageName;
        this.fullClassName = fullClassName;
        this.className = className;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public void setTypeElement(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public void addMethod(OnClickInfo onClickInfo) {
        methods.add(onClickInfo);
    }

    //生成Java代码
    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("// generate code, do not modify!\n");
        builder.append("package ").append(packageName).append(";\n\n");
        builder.append("import android.view.View;\n\n");
        builder.append("import cn.huolala.apt_api.Binder;\n\n");
        builder.append("public class ").append(className).append("_Binder")
                .append(" implements Binder<").append(className).append(">{\n");
        generateMethod(builder);
        builder.append("\n");
        builder.append("}\n");
        return builder.toString();
    }

    private void generateMethod(StringBuilder builder) {
        builder.append("  @Override\n");
        builder.append("  public void inject(").append(className).append(" source) {\n");
        for (int i = 0; i < methods.size(); i++) {
            OnClickInfo onClickInfo = methods.get(i);
            builder.append("        View view").append(i).append(" = source.findViewById(")
                    .append(onClickInfo.getId()).append(");\n");
            builder.append("        view").append(i).append(".setOnClickListener(new View.OnClickListener() {\n")
                    .append("       @Override\n")
                    .append("       public void onClick(View v) {\n")
                    .append("       source.").append(onClickInfo.getMethodName()).append("(v);\n")
                    .append("       }\n")
                    .append("       });\n");
        }
        builder.append("   }\n");
    }
}
