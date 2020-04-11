# table-printer

* 一个通用的打印表格工具

## 使用

### 已经定义好的printer
```$xslt

TablePrinter.DEFAULT;
TablePrinter.FULL;
TablePrinter.SIMPLE;
TablePrinter.CSV;

// 使用
TablePrinter.DEFAULT.render(list)

//自定义printer
TablePrinter myPrinter = TablePrinter.build(TableSetting.build()
            .withShowHeader(false)
            .withOutside(null)
            .withShowNo(false));
            
myPrinter.render(list);                      
```

### 可以设置的属性
```$xslt
private int padding = 1;                                            //左右边距
private Character encase = null;                                    //包含
private boolean equilong = true;                                    //默认等宽
private int maxColWidth = 250;                                      //最大列宽
private boolean showHeader = true;                                  //显示header
private boolean showNo = true;                                      //显示序列号
private String sequenceName = "No.";                                //序列号header
private boolean wordWrap = true;                                    //自动换行

private TableTextAlign textAlign = TableTextAlign.CENTER;           //对齐方式
private TableLineAlign lineAlign = TableLineAlign.CENTER;           //上下对齐方式
private TableTextAlign headerTextAlign = TableTextAlign.CENTER;     //header对齐方式
private TableLineAlign headerLineAlign = TableLineAlign.CENTER;     //header上下对齐方式
private String lineSplit = "\n";                                    //分割字符
private Map<String, String> escapeChars = new HashMap();            //转义字符
private TableBorder outside = new TableBorder('-', '|', '+');       //外边框
private TableBorder inside = new TableBorder('-', '|', '+');        //内边框

#所有属性可以通过withXxx(value)的方式进过DSL链式设值
```

### 元素是列表
```
List list = Lists.newArrayList(
        Lists.newArrayList(1, "name", "test"),
        Lists.newArrayList(1, "name", "test"),
        Lists.newArrayList(1, "name", "test")
);
System.out.println(TablePrinter.DEFAULT.render(list));

#outout
+---+------+------+
| 1 |  2   |  3   |
+---+------+------+
| 1 | name | test |
+---+------+------+
| 1 | name | test |
+---+------+------+
| 1 | name | test |
+---+------+------+

//只打印第0，1列
System.out.println(TablePrinter.DEFAULT.render(list, Lists.newArrayList(0, 1))); 
+---+------+
| 0 |  1   |
+---+------+
| 1 | name |
+---+------+
| 1 | name |
+---+------+
| 1 | name |
+---+------+
```
### 元素是字典
```$xslt
List map = Lists.newArrayList(
        ImmutableMap.of("name", "name_1", "age", 10, "message", "tessssssssssst"),
        ImmutableMap.of("name", "name_1", "age", 10, "message", "tessssssssssst"),
        ImmutableMap.of("name", "name_1", "age", 10, "message", "tessssssssssst")
);
System.out.println(TablePrinter.DEFAULT.render(map));
//output
+--------+-----+----------------+
|  name  | age |    message     |
+--------+-----+----------------+
| name_1 | 10  | tessssssssssst |
+--------+-----+----------------+
| name_1 | 10  | tessssssssssst |
+--------+-----+----------------+
| name_1 | 10  | tessssssssssst |
+--------+-----+----------------+


//只打印name, age列
System.out.println(TablePrinter.DEFAULT.render(map, Lists.newArrayList("name", "age"))); 
+--------+-----+
|  name  | age |
+--------+-----+
| name_1 | 10  |
+--------+-----+
| name_1 | 10  |
+--------+-----+
| name_1 | 10  |
+--------+-----+

```
### 元素是bean
```$xslt
TableSetting setting = TablePrinter.DEFAULT.copySetting();
System.out.println(TablePrinter.DEFAULT.render(setting, TableSetting.NOT_SHOW_HEADER));
// output
+-----------------+----------------------------------------------+
|    lineSplit    |                                              |
|                 |                                              |
+-----------------+----------------------------------------------+
|     padding     |                      1                       |
+-----------------+----------------------------------------------+
|    equilong     |                     true                     |
+-----------------+----------------------------------------------+
|  hexByteJoiner  |    com.google.common.base.Joiner@147ed70f    |
+-----------------+----------------------------------------------+
|    textAlign    |                    CENTER                    |
+-----------------+----------------------------------------------+
|   showHeader    |                     true                     |
+-----------------+----------------------------------------------+
|    wordWrap     |                     true                     |
+-----------------+----------------------------------------------+
| headerTextAlign |                    CENTER                    |
+-----------------+----------------------------------------------+
|  hexLineJoiner  |    com.google.common.base.Joiner@61dd025     |
+-----------------+----------------------------------------------+
|   escapeChars   |                      {}                      |
+-----------------+----------------------------------------------+
|  lineSplitter   |   com.google.common.base.Splitter@124c278f   |
+-----------------+----------------------------------------------+
|     inside      | com.pkest.table.printer.TableBorder@15b204a1 |
+-----------------+----------------------------------------------+
|  sequenceName   |                     No.                      |
+-----------------+----------------------------------------------+
|     encase      |                     NULL                     |
+-----------------+----------------------------------------------+
| headerLineAlign |                    CENTER                    |
+-----------------+----------------------------------------------+
|     showNo      |                    false                     |
+-----------------+----------------------------------------------+
|    lineAlign    |                    CENTER                    |
+-----------------+----------------------------------------------+
|     outside     | com.pkest.table.printer.TableBorder@77167fb7 |
+-----------------+----------------------------------------------+
|   hexSplitter   |   com.google.common.base.Splitter@1fe20588   |
+-----------------+----------------------------------------------+
|   maxColWidth   |                     250                      |
+-----------------+----------------------------------------------+
|      class      |  class com.pkest.table.printer.TableSetting  |
+-----------------+----------------------------------------------+
```

### dump
```$xslt
TableSetting setting = TablePrinter.DEFAULT.copySetting();
System.out.println(TablePrinter.DEFAULT.dump(null));
System.out.println(TablePrinter.DEFAULT.dump(setting));
System.out.println(TablePrinter.DEFAULT.dump(1));
System.out.println(TablePrinter.DEFAULT.dump("string"));
System.out.println(TablePrinter.DEFAULT.dump(Lists.newArrayList("x", "xx")));
System.out.println(TablePrinter.DEFAULT.dump(ImmutableMap.of("id", 1, "name", "xx")));
```

### 简单设置
```$xslt
// 隐藏header输出
System.out.println(TablePrinter.DEFAULT.render(map, TableSetting.NOT_SHOW_HEADER));
+--------+----+----------------+
| name_1 | 10 | tessssssssssst |
+--------+----+----------------+
| name_1 | 10 | tessssssssssst |
+--------+----+----------------+
| name_1 | 10 | tessssssssssst |
+--------+----+----------------+

//显示头并打印行号
System.out.println(TablePrinter.DEFAULT.render(map, TableSetting.SHOW_HEADER | TableSetting.SHOW_NO));
+-----+--------+-----+----------------+
| No. |  name  | age |    message     |
+-----+--------+-----+----------------+
|  1  | name_1 | 10  | tessssssssssst |
+-----+--------+-----+----------------+
|  2  | name_1 | 10  | tessssssssssst |
+-----+--------+-----+----------------+
|  3  | name_1 | 10  | tessssssssssst |
+-----+--------+-----+----------------+
```
### 完整配置
```$xslt
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
//这个是一个csv格式的设置            
System.out.println(TablePrinter.DEFAULT.render(map, setting));

//output
 name,age,message 
 name_1,10,tessssssssssst 
 name_1,10,tessssssssssst 
 name_1,10,tessssssssssst 
```

### 自定义writer输出
```$xslt
PrintWriter writer = new PrintWriter(new File("./target/test.txt"));
TablePrinter.FULL.render(list, Lists.newArrayList(0, 1), writer);
writer.flush();
writer.println();
TablePrinter.SIMPLE.render(data, writer);
writer.flush();
```