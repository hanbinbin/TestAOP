package cn.huolala.asm_plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2023/1/16.
 * PS: Not easy to write code, please indicate.
 */
public class OnClickClassLambdaVisitor extends ClassNode implements Opcodes {
    private ClassVisitor classVisitor;
    private final String methodName = "onClick";
    private final String interfaceName = "Landroid/view/View$OnClickListener;";
    private final String viewDescriptor = "Landroid/view/View;";

    public OnClickClassLambdaVisitor(ClassVisitor classVisitor) {
        super(ASM9);
        this.classVisitor = classVisitor;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        List<InvokeDynamicInsnNode> arrayList = new ArrayList<>();
        for (Object object : methods) {
            MethodNode methodNode = (MethodNode) object;
            if (methodNode.instructions != null) {
                ListIterator listIterator = methodNode.instructions.iterator();
                while (listIterator.hasNext()) {
                    Object node = listIterator.next();
                    if (node instanceof InvokeDynamicInsnNode) {
                        InvokeDynamicInsnNode invokeDynamicInsnNode = (InvokeDynamicInsnNode) node;
                        String nodeName = invokeDynamicInsnNode.name;
                        String nodeDes = invokeDynamicInsnNode.desc;
                        if (nodeName.equals(methodName) && nodeDes.endsWith(interfaceName)) {
                            arrayList.add(invokeDynamicInsnNode);
                        }
                    }
                }
            }
        }

        for (InvokeDynamicInsnNode invokeDynamicInsnNode : arrayList) {
            Handle handle = (Handle) invokeDynamicInsnNode.bsmArgs[1];
            if (handle != null) {
                //找到 lambda 指向的目标方法
                String nameWithDesc = handle.getName() + handle.getDesc();
                for (Object object : methods) {
                    MethodNode methodNode = (MethodNode) object;
                    String methodNameWithDesc = methodNode.name + methodNode.desc;
                    if (nameWithDesc.equals(methodNameWithDesc)) {
                        hookMethod(methodNode);
                    }
                }
            }
        }
        accept(classVisitor);
    }

    private void hookMethod(MethodNode methodNode) {
        Type[] types = Type.getArgumentTypes(methodNode.desc);
        int viewArgumentIndex = 0;
        if (types.length == 0) {
            viewArgumentIndex = -1;
        } else {
            for (int i = 0; i < types.length; i++) {
                Type type = types[i];
                if (type.getDescriptor().endsWith(viewDescriptor)) {
                    viewArgumentIndex = i;
                }
            }
        }
        if (viewArgumentIndex >= 0) {
            InsnList instructions = methodNode.instructions;
            if (instructions != null && instructions.size() > 0) {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(
                                Opcodes.ALOAD, getVisitPosition(
                                types,
                                viewArgumentIndex,
                                (methodNode.access & Opcodes.ACC_STATIC) != 0)
                        )
                );

                list.add(new LdcInsnNode("ASM"));
                list.add(new LdcInsnNode("lambda hello"));
                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false));

                LabelNode labelNode = new LabelNode();
                list.add(new JumpInsnNode(Opcodes.IFNE, labelNode));
                list.add(new InsnNode(Opcodes.RETURN));
                list.add(labelNode);
                instructions.insert(list);
            }
        }
    }

    private int getVisitPosition(Type[] types, int viewArgumentIndex, boolean isStaticMethod) {
        if (viewArgumentIndex < 0 || viewArgumentIndex >= types.length) {
            throw new Error("getVisitPosition error");
        }
        if (viewArgumentIndex == 0) {
            return isStaticMethod ? 0 : 1;
        } else {
            return getVisitPosition(types, viewArgumentIndex - 1, isStaticMethod) + types[viewArgumentIndex - 1].getSize();
        }
    }
}
