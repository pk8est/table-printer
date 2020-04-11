package com.pkest.table.printer;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.beanutils.BeanMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author 360733598@qq.com
 * @date 2019/2/22 23:49
 */
public class TableUtils {

    final static Gson gson = TableUtils.setupGson();

    public static Gson getGson() {
        return gson;
    }

    private static Gson setupGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

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
