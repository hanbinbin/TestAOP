package cn.huolala.mytestapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import cn.huolala.mytestapplication.bean.UserInfo;
import cn.huolala.mytestapplication.chain.ModifyTask;
import cn.huolala.mytestapplication.utils.TelephoneNumberUtils;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 9/23/21.
 * PS: Not easy to write code, please indicate.
 */
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.output).setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SecondActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                testFileOutputStream();
            }
        });

        findViewById(R.id.input).setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SecondActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                testFileInputStream();
            }
        });

        TextView textView = findViewById(R.id.telephone_recognize);
        //开始识别电话号码
        findViewById(R.id.telephone_recognize).setOnClickListener(v -> {
            String beforeStr = "这是测试数据";
            List<String> afterStr = TelephoneNumberUtils.readTelNumByPattern(beforeStr);
            dealWith(beforeStr, afterStr, textView);
        });

        //测试责任链模式
        findViewById(R.id.chain).setOnClickListener(v -> {
            //开启操作用户
            new ModifyTask().execute(new UserInfo());
        });
    }

    private void dealWith(String str, List<String> patternList, TextView textView) {
        if (TextUtils.isEmpty(str) || patternList == null || textView == null) {
            return;
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(str);
        for (String pattern : patternList) {
            if (!str.contains(pattern)) {
                continue;
            }
            int start = str.indexOf(pattern);
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //点击事件
                    Log.e("SecondActivity", TelephoneNumberUtils.doWithTelNum(pattern));
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    //设置文件颜色
                    ds.setColor(textView.getResources().getColor(R.color.teal_200));
                    // 去掉下划线
                    ds.setUnderlineText(false);
                }

            }, start, start + pattern.length(), 0);
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    private void testFileOutputStream() {
        FileOutputStream fos = null;
        try {
            String path = getExternalFilesDir(Environment.DIRECTORY_DCIM).toString();
            File file = new File(path + "/fos.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file.getPath());
            // 调用write()方法
            //fos.write(97); //97 -- 底层二进制数据	-- 通过记事本打开 -- 找97对应的字符值 -- a （是通过ASCII码表得出的对应字符）
            // fos.write(57);
            // fos.write(55);

            //public void write(byte[] b):写一个字节数组
            byte[] bys = {97, 98, 99, 100, 101}; //abcde
            fos.write(bys);

            byte[] bys1 = {-26, -75, -117, -24, -81, -107}; //测试
            fos.write(bys1);

            String str = "测试字符";
            byte[] testStr = str.getBytes();
            for (byte byt : testStr) {
                Log.e("测试字符", "" + byt);
            }
            fos.write(testStr);

            //中文字符转byte数组的过程：
//            例如;中 这个字是如何转换成一个byte数组的？
//            String str = "中";
//            byte [] bs = str.getBytes("UTF-8");
//            首先，任意一个字符都定义在统一的unicode编码表中，unicode有65535个字符，几乎包含了所有的语言。所以‘中’在unicode中的编码为\u4E2D(十六机制)对应10进制为20013.
//                    然后看下utf-8计算byte数组的算法：
//            var4[0] = (byte)(224 | var8 >> 12);
//            var4[1] = (byte)(128 | var8 >> 6 & 63);
//            var4[2] = (byte)(128 | var8 & 63);
//
//            第0个数组 20013>> 12 结果再和224进行或运算
//            20013二进制： 0 1 0 0 1 1 1 0 0 0 1 0 1 1 0 1
//            向右移动12位： 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0
//            224的二进制： 0 0 0 0 0 0 0 0 1 1 1 0 0 0 0 0
//                    | 运算结果为： 0 0 0 0 0 0 0 0 1 1 1 0 0 1 0 0
//            11100100的十进制为： -28
//
//            第1个数组 20013>> 6 结果再和63与运算 再和128或运算
//            20013二进制:  0 1 0 0 1 1 1 0 0 0 1 0 1 1 0 1
//            向右移动6位:  0 0 0 0 0 0 0 1 0 0 1 1 1 0 0 0
//            63的二进制:   0 0 0 0 0 0 0 0 0 0 1 1 1 1 1 1
//                    & 运算结果为： 0 0 0 0 0 0 0 0 0 0 1 1 1 0 0 0
//            128的二进制为： 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
//                    | 运算结果为： 10111000
//            十进制为： -72
//
//            第2个数组 20013& 63 再和128或运算
//            20013二进制： 0 1 0 0 1 1 1 0 0 0 1 0 1 1 0 1
//            63的二进制： 0 0 0 0 0 0 0 0 0 0 1 1 1 1 1 1
//                    & 运算结果为： 0 0 0 0 0 0 0 0 0 0 1 0 1 1 0 1
//            128的二进制为： 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
//                    | 运算结果为： 10101101
//            十进制为： -83
//
//            这样3个byte数组的数字分别是 -28 -72 -83 ，大家可以通过java运行来检验下结果是否正确，
//            其实第0位数组算的是16位字节中的前4位，第1位数组放的5-10位字节，第2位数组放的是11-16位的字节，既 4+6+6 = 16 byte数组按照这种方式切分。

            //public void write(byte[] b,int off,int len):写一个字节数组的一部分
            fos.write(bys, 1, 3);
            String str1 = "\n应用名称            应用标识                        应用版本     发起下载数    下载成功数       成功率(%)\n";
            String str2 = "货运司机    com.lalamove.huolala.driver            6.1.57        59382         53796        90.6\n";
            String str3 = "货运司机    com.lalamove.huolala.driver            6.1.21        51537         39422        76\n";
            String str4 = "小拉司机     com.xiaolachuxing.driver              1.0.28         4921         2194         45\n";
            String str5 = "小拉司机     com.xiaolachuxing.driver              1.0.30         6782         5667         84.4\n";
            String str6 = "小拉用户     com.xiaolachuxing.user                1.2.3          15042         7858        52.2\n";
            String str7 = "小拉用户     com.xiaolachuxing.user                1.2.4          7427          3677        49.5\n";
            fos.write(str1.getBytes());
            fos.write(str2.getBytes());
            fos.write(str3.getBytes());
            fos.write(str4.getBytes());
            fos.write(str5.getBytes());
            fos.write(str6.getBytes());
            fos.write(str7.getBytes());
            byte[] bytes = {123, 34, 101, 110, 116, 105, 116, 121, 34, 58, 123, 34, 97, 99, 116, 105, 111, 110, 34, 58, 49, 44, 34, 99, 104, 97, 116, 84, 121, 112, 101, 34, 58, 50, 44, 34, 99, 111, 110, 116, 101, 110, 116, 34, 58, 34, 34, 44, 34, 102, 97, 99, 101, 85, 114, 108, 34, 58, 34, 34, 44, 34, 110, 105, 99, 107, 110, 97, 109, 101, 34, 58, 34, -24, -82, -94, -27, -115, -107, -27, -80, -66, -27, -113, -73, 56, 56, 57, 48, 49, 56, 48, 34, 44, 34, 115, 101, 110, 100, 84, 105, 109, 101, 34, 58, 48, 44, 34, 115, 101, 110, 100, 101, 114, 34, 58, 34, 49, 52, 53, 55, 54, 50, 51, 50, 51, 52, 51, 49, 55, 57, 53, 57, 49, 54, 56, 34, 44, 34, 117, 117, 105, 100, 34, 58, 110, 117, 108, 108, 44, 34, 118, 101, 114, 115, 105, 111, 110, 34, 58, 49, 125, 125};
            fos.write(bytes);
            //释放资源
            fos.close();
            Log.e("123254533", file.getName());

            //截取字符串
            String testPath = "argus-offline-20211223.glog";
            String stringDate =
                    testPath.substring(testPath.lastIndexOf("-") + 1, testPath.indexOf(".glog"));
            Log.e("123254533", stringDate);

            //测试位移运算
            int a = 1;
            int b = 1 << 1;
            int c = 1 << 2;
            int d = 1 << 3;
            int e = 1 << 4;
            Log.e("123254533", "a=" + a + " : b=" + b + " : c=" + c + " : d=" + d + " : e=" + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testFileInputStream() {
        try {
            String path = getExternalFilesDir(Environment.DIRECTORY_DCIM).toString();
            File file = new File(path + "/fos.txt");
            if (!file.exists()) {
                Toast.makeText(this, "文件不存在，无法读取文件", Toast.LENGTH_SHORT).show();
                return;
            }
            FileInputStream fis = new FileInputStream(file.getPath());

            byte[] bys = new byte[1024];
            int len = 0;
            while ((len = fis.read(bys)) != -1) {
                Log.e("fis: ", new String(bys, 0, len));
            }
            // 释放资源
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
