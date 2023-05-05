package cn.huolala.mytestapplication.apm;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2022/10/24.
 * PS: Not easy to write code, please indicate.
 */
public class ApmSlowMethodStack {
    int mCount = 0;
    MethodItem mMethodItem;
    int maxCostTime = 0;
    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
    Handler handler = MatrixHandlerThread.getDefaultHandler();
    Pattern stackFirst = Pattern.compile("at\\s{2}(?!\\(|java|android|androidx|com.android|com.java|sun.)(.*)\r\n");
    Pattern stackSecond = Pattern.compile("(at\\s{2})(?!\\()(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public interface TrimTreeNodeFilter {
        int getFilterMinCost();//过滤掉小于FilterMinCost的方法

        float getWeightCost();//慢方法阀值*耗时权重

        float getLevelTreeCount();//每个层级采集子树数量

        int getTreeNodeMaxCount(); //最多节点
    }

    public void matchSampleRate() {
//        String mainStr = "// 崩溃线程\n" +
//                "\"main\" prio=5 tid=1 Sleeping\n" +
//                "  | group=\"main\" sCount=1 dsCount=0 flags=1 obj=0x72754c48 self=0x7d01ac3e00\n" +
//                "  | sysTid=21707 nice=-4 cgrp=default sched=0/0 handle=0x7d873419a8\n" +
//                "  | state=S schedstat=( 795542889 21183473 756 ) utm=62 stm=17 core=2 HZ=100\n" +
//                "  | stack=0x7ffbbe6000-0x7ffbbe8000 stackSize=8MB\n" +
//                "  | held mutexes=\n" +
//                "  at java.lang.Thread.sleep(Native method)\n" +
//                "  - sleeping on <0x022a266e> (a java.lang.Object)\n" +
//                "  at java.lang.Thread.sleep(Thread.java:373)\n" +
//                "  - locked <0x022a266e> (a java.lang.Object)\n" +
//                "  at java.lang.Thread.sleep(Thread.java:314)\n" +
//                "  at hcrash.sample.MainActivity.testAnrInput_onClick(MainActivity.java:91)\n" +
//                "  at hcrash.sample.MainActivity.testNativeCrashInAnotherActivity_onClick(MainActivity.java:66)\n" +
//                "  at java.lang.reflect.Method.invoke(Native method)\n" +
//                "  at androidx.appcompat.app.AppCompatViewInflater$DeclaredOnClickListener.onClick(AppCompatViewInflater.java:409)\n" +
//                "  at android.view.View.performClick(View.java:6312)\n" +
//                "  at android.view.View$PerformClick.run(View.java:24927)\n" +
//                "  at android.os.Handler.handleCallback(Handler.java:790)\n" +
//                "  at android.os.Handler.dispatchMessage(Handler.java:99)\n" +
//                "  at android.os.Looper.loop(Looper.java:192)\n" +
//                "  at android.app.ActivityThread.main(ActivityThread.java:6872)\n" +
//                "  at java.lang.reflect.Method.invoke(Native method)\n" +
//                "  at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:549)\n" +
//                "  at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:817)\n" +
//                "java stacktrace:\n" +
//                "null";
        Pattern pattern = Pattern.compile("\\s*Windows(?!95|98|NT|2000)\\s*"); //表示能匹配“Windows3.1”中的“Windows”，但不能匹配“Windows2000”中的“Windows”。
        test_match("     Windows  ", pattern);//result="      Windows  ";
        test_match("   Windows3.1  ", pattern);//result="    Windows";
        test_match(" Windows2000 ", pattern);//no match
        test_match("  Windows98  ", pattern);//no match
        test_match("  Windows95  ", pattern);//no match
    }

    public void testMatchIsSystemTag() {
        String str = "at  android.os.Handler.dispatchMessage (Landroid.os.Message;)V cost = 2799 ms count =  1\r\n" + "     at  org.eclipse.paho.android.service.MqttService.onStartCommand (Landroid.content.Intent;II)I cost = 2 ms count =  1\r\n" + "          at  org.eclipse.paho.android.service.MqttService.onStartCommand (Landroid.content.Intent;II)I cost = 2 ms count =  1\r\n" + "               at  org.eclipse.paho.android.service.MqttService.registerBroadcastReceivers ()V cost = 2 ms count =  1\r\n";
        test_match(str, stackFirst);
    }

    // 司机慢方法top 20 堆栈信息
    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221117/6PnlkzGD_slowMethod_stackUrl_1668672205827.txt
    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221117/DXGZY7XN_slowMethod_stackUrl_1668675705040.txt
    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221117/Nl8V2j3N_slowMethod_stackUrl_1668674120042.txt
    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221117/mDybWWGo_slowMethod_stackUrl_1668670885081.txt
    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221117/Nl8mWxvN_slowMethod_stackUrl_1668676463432.txt
    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221117/goLGQ7YD_slowMethod_stackUrl_1668640821123.txt
    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221117/Nxe5JgyD_slowMethod_stackUrl_1668673743913.txt
    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221117/oQaOl2go_slowMethod_stackUrl_1668671122281.txt
    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221116/N53X8Xlo_slowMethod_stackUrl_1668574661495.txt
    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221117/o1YajABo_slowMethod_stackUrl_1668675194739.txt
    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221117/D3l48zRo_slowMethod_stackUrl_1668648842137.txt
    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221117/N2Qxz3E6_slowMethod_stackUrl_1668651781541.txt
    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221115/oqyr8zOo_slowMethod_stackUrl_1668511752014.txt
    //
    // 企业慢方法 堆栈信息
    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.eclient-android/20221115/N2xE99Eo_slowMethod_stackUrl_1668476189276.txt
    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.eclient-android/20221117/6p4Wy146_slowMethod_stackUrl_1668668536930.txt
    //
    //
    //
    //
    //
    //
    public void readContentFromServer() {
        handler.post(() -> {
            Request request = new Request.Builder()
                    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.eclient-android/20221025/NGJaJKYv_slowMethod_stackUrl_1666661990757.txt
                    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.eclient-android/20221025/Dryxlm0N_slowMethod_stackUrl_1666659645998.txt
                    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.eclient-android/20221025/Nnb54M3N_slowMethod_stackUrl_1666690365215.txt
                    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221010/mNnMrRQ6_slowMethod_stackUrl_1665396771041.txt
                    // https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221024/NZE9BLj6_slowMethod_stackUrl_1666616298294.txt

                    .url("https://mcv-crash.oss-cn-shenzhen.aliyuncs.com/prd/com.lalamove.huolala.driver-android/20221024/NZE9BLj6_slowMethod_stackUrl_1666616298294.txt")
                    .build();
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                if (response != null && response.isSuccessful() && response.body() != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(response.body().byteStream());
                    char[] chars = new char[1024];
                    int len;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((len = inputStreamReader.read(chars)) != -1) {
                        stringBuilder.append(chars, 0, len);
                    }
                    long start = System.currentTimeMillis();
                    Log.e("start", "" + start);
                    String[] stack = stringBuilder.toString().split(Constant.line_tag);
                    Log.e("stack", "stack.length = " + (stack.length - 1));
                    TreeNode root = new TreeNode(null, null);
                    stackToTree(stack, root);
                    //裁剪堆栈
                    trimTreeNode(root);
                    //获取最耗时的方法
                    MethodItem method = getMaxCostMethod(root);
                    Log.e("max cost method", method == null ? "" : method.methodName.trim());
                    //拼接字符串，还原堆栈信息
                    StringBuilder stringBuilderResult = new StringBuilder();
                    stringBuilderResult.append(Constant.line_tag);
                    treeToStack(root, stringBuilderResult);
                    Log.e("result", stringBuilderResult.toString());
                    Log.e("end", "cost time = " + (System.currentTimeMillis() - start));
                    //使用正则进行匹配,获取reason行，或者标签 tag
                    extracted(stringBuilderResult.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    private void stackToTree(String[] stack, TreeNode root) {
        TreeNode lastNode = null;
        for (String methodName : stack) {
            //一般第一行为空字符串
            if (TextUtils.isEmpty(methodName)) {
                continue;
            }
            int methodDepth = getMethodDepth(methodName);
            int methodCost = getMethodDurTime(methodName);
            MethodItem methodItem = new MethodItem(methodName, methodCost, methodDepth);

            TreeNode node = new TreeNode(methodItem, lastNode);
            if (null == lastNode && node.depth() != 0) {
                Log.e("TAG", "[stackToTree] begin error! why the first node'depth is not 0!");
                return;
            }
            int depth = node.depth();
            if (lastNode == null || depth == 0) {
                root.add(node); //根目录
            } else if (lastNode.depth() >= depth) {
                while (null != lastNode && lastNode.depth() > depth) {
                    lastNode = lastNode.father;
                }
                if (lastNode != null && lastNode.father != null) {
                    node.father = lastNode.father;
                    lastNode.father.add(node);
                }
            } else {
                lastNode.add(node);
            }
            lastNode = node;
        }
    }

    /**
     * trim Tree Node
     *
     * @param root TreeNode
     */
    public void trimTreeNode(TreeNode root) {
        TrimTreeNodeFilter trimTreeNodeFilter = new TrimTreeNodeFilter() {
            @Override
            public int getFilterMinCost() {
                return 5; //5ms
            }

            @Override
            public float getLevelTreeCount() {
                return 5; //每个层级保留堆栈
            }

            @Override
            public float getWeightCost() {
                return 0.3f * 1000; //满足慢方法阀值一定比例的耗时的treeNode也采集
            }

            @Override
            public int getTreeNodeMaxCount() {
                return 60; //最大上限保留堆栈里面方法数
            }
        };
        long start = System.currentTimeMillis();
        mCount = 0;
        innerTrimTreeNode(root, trimTreeNodeFilter);
        Log.e("TrimTreeNode cost time", (System.currentTimeMillis() - start) + "ms");
    }

    private void innerTrimTreeNode(TreeNode root, TrimTreeNodeFilter trimTreeNodeFilter) {
        //先对TreeNode进行排序
        Collections.sort(root.children);
        for (int i = 0; i < root.children.size(); i++) {
            TreeNode node = root.children.get(i);
            if (null == node) {
                continue;
            }
            //小于5ms清除
            if (node.durTime() < trimTreeNodeFilter.getFilterMinCost()) {
                node.clear();
                continue;
            }
            //同一层级权重取前五 或者 节点耗时 大于 慢方法阀值*0.3   都去采集
            if (i < trimTreeNodeFilter.getLevelTreeCount() || (node.durTime() > trimTreeNodeFilter.getWeightCost())) {
                if (mCount < trimTreeNodeFilter.getTreeNodeMaxCount()) {
                    mCount++;
                    innerTrimTreeNode(node, trimTreeNodeFilter);
                } else {
                    node.clear();
                }
            } else {
                node.clear();
            }
        }
    }

    /**
     * 获取最耗时的方法
     */
    private MethodItem getMaxCostMethod(TreeNode root) {
        mMethodItem = null;
        maxCostTime = 0;
        innerGetMaxCostMethod(root);
        return mMethodItem;
    }

    /**
     * 获取最耗时的方法, 内部递归调用
     */
    private void innerGetMaxCostMethod(TreeNode root) {
        //先对TreeNode进行排序
        for (int i = 0; i < root.children.size(); i++) {
            TreeNode node = root.children.get(i);
            if (node == null) {
                continue;
            }
            int childrenTotalCostTime = 0;
            for (int j = 0; j < node.children.size(); j++) {
                TreeNode childNode = node.children.get(j);
                if (childNode == null) {
                    continue;
                }
                childrenTotalCostTime = childrenTotalCostTime + childNode.durTime();
            }
            int nodeCostTime = node.durTime() - childrenTotalCostTime;
            if (nodeCostTime > maxCostTime) {
                maxCostTime = nodeCostTime;
                mMethodItem = node.methodItem;
            }
            innerGetMaxCostMethod(node);
        }
    }

    private void treeToStack(TreeNode root, StringBuilder stringBuilder) {
        for (int i = 0; i < root.children.size(); i++) {
            TreeNode node = root.children.get(i);
            if (node == null) {
                continue;
            }
            //第一个筛选条件
            if (node.methodItem != null) {
                stringBuilder.append(node.methodName());
                stringBuilder.append(Constant.line_tag);
            }
            //第二个筛选条件
            if (!node.children.isEmpty()) {
                treeToStack(node, stringBuilder);
            }
        }
    }

    /**
     * 获取当前方法栈的深度
     */
    public int getMethodDepth(String methodName) {
        String[] str = methodName.split(Constant.at_tag);
        if (TextUtils.isEmpty(str[0])) {
            return 0;
        }
        int length = str[0].length();
        return length / Constant.depth_tag.length();
    }

    /**
     * 获取执行当前方法耗时
     */
    public int getMethodDurTime(String methodName) {
        String[] str1 = methodName.split(Constant.cost_tag1);
        if (str1 == null || str1.length < 2) {
            return 0;
        }
        String string1 = str1[1];
        if (TextUtils.isEmpty(string1)) {
            return 0;
        }
        String[] str2 = string1.split(Constant.cost_tag2);
        if (str2 == null || str2.length < 1) {
            return 0;
        }
        String string2 = str2[0];
        if (TextUtils.isEmpty(string2)) {
            return 0;
        }
        try {
            return Integer.parseInt(string2.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public static final class MethodItem {
        public String methodName;
        public int durTime;
        public int depth;

        public MethodItem(String methodName, int durTime, int depth) {
            this.methodName = methodName;
            this.durTime = durTime;
            this.depth = depth;
        }

        @Override
        public String toString() {
            return "methodName = " + methodName + ", durTime = " + durTime + ", depth= " + depth;
        }
    }

    /**
     * it's the node for the stack tree
     */
    public static final class TreeNode implements Comparable<TreeNode> {
        MethodItem methodItem;
        TreeNode father;

        LinkedList<TreeNode> children = new LinkedList<>();

        public TreeNode(MethodItem methodItem, TreeNode father) {
            this.methodItem = methodItem;
            this.father = father;
        }

        public int depth() {
            return null == methodItem ? 0 : methodItem.depth;
        }

        public int durTime() {
            return null == methodItem ? 0 : methodItem.durTime;
        }

        public String methodName() {
            return null == methodItem ? "" : methodItem.methodName;
        }

        public void add(TreeNode node) {
            children.addFirst(node);
        }

        public void clear() {
            methodItem = null;
            children.clear();
        }

        @Override
        public int compareTo(TreeNode o) {
            return o.durTime() - durTime();
        }
    }

    private void extracted(String mainStr) {
        String crashR = "";
        // 1.优先匹配业务堆栈 at xxx
        Matcher matcher = stackFirst.matcher(mainStr);
        boolean isFind = matcher.find();
        if (!isFind) {
            Log.e("ApmSlowMethodStack", "crashR 1 :");
            matcher = stackSecond.matcher(mainStr);
            isFind = matcher.find();
        }
        if (isFind) {
            // 2.取第一行
            String str = matcher.group();
            int tmp = str.indexOf(Constant.at_tag, 2);
            if (tmp > 2) {
                crashR = matcher.group().substring(2, tmp);
            } else {
                crashR = matcher.group();
            }
            Log.e("ApmSlowMethodStack", "crashR 11 :" + crashR);
        } else {
            int index = mainStr.indexOf(Constant.at_tag);
            String tmp = mainStr.substring(index);
            int i = tmp.indexOf(Constant.at_tag, 2);
            if (i > 2) {
                crashR = tmp.substring(2, i);
            } else {
                crashR = tmp.substring(2, mainStr.length());
            }
            Log.e("ApmSlowMethodStack", "crashR 22 .... " + crashR);
        }
    }

    private void test_match(String originWord, Pattern pattern) {
        Matcher matcher = pattern.matcher(originWord);
        boolean isFind = matcher.find();
        if (isFind) {
            String str = matcher.group();
            Log.e("pattern", "match success; result = " + str);
        } else {
            Log.e("pattern", "match fail");
        }
    }
}
