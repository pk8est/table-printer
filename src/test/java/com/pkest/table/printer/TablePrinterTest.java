package com.pkest.table.printer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author 360733598@qq.com
 * @date 2020/4/10 9:48
 */
public class TablePrinterTest {

    private List listData;
    private List mapData;

    @Before
    public void setUp(){
        listData = Lists.newArrayList(
                Lists.newArrayList(1, "tessss\r\nsss\nsssst\nessss\nessss\nessss\nessss\nessss\nessss\nessss",1, "tessssss\nssssst",1, "tessssssssssst",1, "tessssssssssst",1, "tessssssssssst",1, "tessssssssssst"),
                Lists.newArrayList(1, "tessssssssssst",1, "tessssssssssst",1, "tessssssssssst",1, "tessssssssssst",1, "tessssssssssst",1, "tessssssssssst"),
                Lists.newArrayList(1, "tessssssssssst",1, "tessssssssssst",1, "tessssssssssst",1, "tessssssssssst",1, "tessssssssssst",1, "tessss'ssssssst")
        );
        mapData = Lists.newArrayList(
                ImmutableMap.of("name", "name_1", "age", 10, "message", "tessssssssssst"),
                ImmutableMap.of("name", "name_1", "age", 10, "message", "tessssssssssst"),
                ImmutableMap.of("name", "name_1", "age", 10, "message", "tessssssssssst")
        );
    }

    @Test
    public void printSimple() throws IOException {
        TableSetting setting = TablePrinter.DEFAULT.copySetting().withShowHeader(false);
        System.out.println(TablePrinter.DEFAULT.render(listData.get(0), setting));
        System.out.println(TablePrinter.DEFAULT.render(mapData.get(0), TableSetting.NOT_SHOW_HEADER | TableSetting.NOT_SHOW_NO).toString());
        //System.out.println(TablePrinter.DEFAULT.render(TableSetting.build(), false).toString());
    }

    @Test
    public void printList() throws IOException {
        System.out.println(TablePrinter.DEFAULT.render(listData).toString());
        System.out.println(TablePrinter.CSV.render(listData, Lists.newArrayList(1, 2)).toString());
    }

    @Test
    public void printMap() throws IOException {
        System.out.println(TablePrinter.FULL.render(mapData).toString());
        System.out.println(TablePrinter.DEFAULT.render(mapData, Lists.newArrayList("name", "age")).toString());
    }

    @Test
    public void printObject() throws IOException {
        TableSetting setting = TablePrinter.DEFAULT.copySetting();
        System.out.println(TablePrinter.FULL.render(Lists.newArrayList(setting, setting)).toString());
        System.out.println(TablePrinter.FULL.render(Lists.newArrayList(setting, setting), Lists.newArrayList("lineSplit", "padding", "equilong", "hexByteJoiner")).toString());
    }


    @Test
    public void printWriter() throws IOException {
        PrintWriter writer = new PrintWriter(new File("./target/test.txt"));
        TablePrinter.FULL.render(listData, Lists.newArrayList(0, 1), writer);
        writer.flush();
        writer.println();
        TablePrinter.SIMPLE.render(mapData, writer);
        writer.flush();

    }

    @Test
    public void printSimpleObject() throws IOException {
        TableSetting setting = TablePrinter.DEFAULT.copySetting();
        System.out.println(TablePrinter.DEFAULT.render(setting, TableSetting.NOT_SHOW_HEADER));
        System.out.println(TablePrinter.DEFAULT.dump(setting));
        //System.out.println(TablePrinter.FULL.render(Lists.newArrayList(setting, setting), Lists.newArrayList("lineSplit", "padding", "equilong", "hexByteJoiner")).toString());

    }

    @Test
    public void printDump() throws IOException {
        TableSetting setting = TablePrinter.DEFAULT.copySetting();
        System.out.println(TablePrinter.DEFAULT.dump(null));
        System.out.println(TablePrinter.DEFAULT.dump(setting));
        System.out.println(TablePrinter.DEFAULT.dump(1));
        System.out.println(TablePrinter.DEFAULT.dump("string"));
        System.out.println(TablePrinter.DEFAULT.dump(Lists.newArrayList("x", "xx")));
        System.out.println(TablePrinter.DEFAULT.dump(ImmutableMap.of("id", 1, "name", "xx")));
    }

    @Test
    public void testSetting() throws IOException {
        List list = Lists.newArrayList(
                Lists.newArrayList(1, "name", "test"),
                Lists.newArrayList(1, "name", "test"),
                Lists.newArrayList(1, "name", "test")
        );
        List map = Lists.newArrayList(
                ImmutableMap.of("name", "name_1", "age", 10, "message", "tessssssssssst"),
                ImmutableMap.of("name", "name_1", "age", 10, "message", "tessssssssssst"),
                ImmutableMap.of("name", "name_1", "age", 10, "message", "tessssssssssst")
        );

        TableSetting setting = TableSetting.build()
                .withShowNo(false)
                .withPadding(0)
                .withLineSplit(null)
                .withMaxColWidth(-1)
                .appendEscapeChars("\n", "\\\\n")
                .withTextAlign(TableTextAlign.LEFT)
                .withEquilong(false)
                .withInside(new TableBorder(' ', ',', ' '))
                .withOutside(null);
        System.out.println(TablePrinter.DEFAULT.render(map, setting));
        //System.out.println(TablePrinter.DEFAULT.render(map, TableSetting.SHOW_HEADER | TableSetting.SHOW_NO));
    }

}
