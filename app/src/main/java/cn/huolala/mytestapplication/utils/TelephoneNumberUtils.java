package cn.huolala.mytestapplication.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 11/18/21.
 * PS: Not easy to write code, please indicate.
 */
public class TelephoneNumberUtils {

    //通过正则表达式识别字符串中的电话号码，并返回电话号码的集合
    public static List<String> readTelNumByPattern(String str) {
        //电话号码集合
        List<String> telNo = new ArrayList<>();
        if (null == str || str.length() <= 0) {
            return telNo;
        }

        //识别手机号码的正则表达式
//        Pattern cellPhonePattern = Pattern.compile("(\\+86 )?((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}");
        //识别座机号码的正则表达式
//        Pattern landLinePattern = Pattern.compile("(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)");
//        Matcher landLineMatcher = landLinePattern.matcher(str);
//        while (landLineMatcher.find()) {
//            telNo.add(landLineMatcher.group());
//        }


//        Pattern cellPhonePattern = Pattern.compile("((\\d*)((\\(?\\d*\\)?-?)(\\+\\d* ?)?)\\d{0,15}){5,15}");
        Pattern cellPhonePattern = Pattern.compile("((\\d*)((\\(?\\d*\\)? {0,2}-? {0,2})(\\+\\d* {0,2})?( {0,2}-?)?)\\d{0,15}){5,15}");
        Matcher cellPhoneMatcher = cellPhonePattern.matcher(str);

//        Pattern cellPhonePatternSecond = Pattern.compile("((\\(\\d{3,4}\\) *)|(\\d{3,4} {0,2}- {0,2})|(\\+86 ?))?(\\d{7,11})");
        Pattern cellPhonePatternSecond = Pattern.compile("( *)(\\+86 ?)?1\\d{10}");
        while (cellPhoneMatcher.find()) {
            String dealWithFind = doWithTelNum(cellPhoneMatcher.group());
            if (dealWithFind.length() >= 5 && dealWithFind.length() <= 15) {
                telNo.add(cellPhoneMatcher.group());
            } else {
                //处理多组号码中间有空格 或 者 换行符的情况
                Matcher cellPhoneSecondMatcher = cellPhonePatternSecond.matcher(cellPhoneMatcher.group());
                while (cellPhoneSecondMatcher.find()){
                    String dealWithFindSecond = doWithTelNum(cellPhoneSecondMatcher.group());
                    telNo.add(dealWithFindSecond);
                }
            }
        }
        return telNo;
    }

    //讲电话号码处理为纯数字
    public static String doWithTelNum(String strTelNo) {
        StringBuilder sbrDialNo = new StringBuilder();
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(strTelNo);
        while (m.find()) {
            sbrDialNo.append(m.group());
        }
        return sbrDialNo.toString();
    }
}
