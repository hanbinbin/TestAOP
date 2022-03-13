package cn.huolala.asm_plugin;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2/27/22.
 * PS: Not easy to write code, please indicate.
 */
public class OnClickMethodVisitor extends MethodVisitor implements Opcodes {

    public OnClickMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM9, methodVisitor);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        mv.visitLdcInsn("ASM");
        mv.visitLdcInsn("hello");
        mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(POP);
    }
}
