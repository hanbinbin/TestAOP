package cn.huolala.apt_compiler;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2/26/22.
 * PS: Not easy to write code, please indicate.
 */
public class OnClickInfo {
    private int id;
    private String methodName;

    OnClickInfo(int id, String methodName) {
        this.id = id;
        this.methodName = methodName;
    }

    public int getId() {
        return id;
    }

    public String getMethodName() {
        return methodName;
    }
}
