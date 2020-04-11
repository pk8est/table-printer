package com.pkest.table.printer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 360733598@qq.com
 * @date 2019/2/22 23:49
 */
public class TableUtils {


    public static List<String> splitStringByLength(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return splitStringByLength(inputString, length, size);
    }


    public static List<String> splitStringByLength(String inputString, int length, int size) {
        List<String> list = new ArrayList();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }


    public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f);
        } else {
            return str.substring(f, t);
        }
    }

}
