package com.pkest.table.printer;

import org.apache.commons.lang3.StringUtils;
import org.fusesource.jansi.AnsiString;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static jline.console.WCWidth.wcwidth;

/**
 * @author 360733598@qq.com
 * @date 2019/2/22 23:49
 */
public class TableUtils {


    public static List<String> splitStringByLength(String inputString, int length) {
        int pos = 0;
        List<String> list = new ArrayList();
        AnsiString ansiString = new AnsiString(inputString);
        CharSequence plain = ansiString.getPlain();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plain.length(); i++) {
            sb.append(plain.charAt(i));
            pos += max(wcwidth(plain.charAt(i)), 0);
            if(pos >= length){
                list.add(sb.toString());
                sb = new StringBuilder();
                pos = 0;
            }
        }
        if(pos != 0){
            list.add(sb.toString());
        }
        return list;
    }

    public static String escape(String input, String encase, String replace){
        if(StringUtils.isNotBlank(input)){
            return input.replace(encase, replace);
        }
        return input;
    }

    public static boolean isWrapClass(Class clz) {
        try {
            return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

}
