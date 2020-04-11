package com.pkest.table.printer;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 360733598@qq.com
 * @date 2020/4/10 21:09
 */
public class TableSetting {

    public static final int EQUILONG = 1 << 0;
    public static final int NOT_EQUILONG = 1 << 1;
    public static final int SHOW_HEADER = 1 << 2;
    public static final int NOT_SHOW_HEADER = 1 << 3;
    public static final int SHOW_NO = 1 << 4;
    public static final int NOT_SHOW_NO = 1 << 5;
    public static final int WORD_WRAP = 1 << 6;
    public static final int NOT_WORD_WRAP = 1 << 7;
    public static final int TEXT_ALIGN_LEFT = 1 << 8;
    public static final int TEXT_ALIGN_CENTER = 1 << 9;
    public static final int TEXT_ALIGN_RIGHT = 1 << 10;
    public static final int LINE_ALIGN_TOP = 1 << 11;
    public static final int LINE_ALIGN_CENTER = 1 << 12;
    public static final int LINE_ALIGN_BOTTOM = 1 << 13;
    public static final int TABLE_TEXT_ALIGN_LEFT = 1 << 14;
    public static final int TABLE_TEXT_ALIGN_CENTER = 1 << 15;
    public static final int TABLE_TEXT_ALIGN_RIGHT = 1 << 16;
    public static final int TABLE_LINE_ALIGN_TOP = 1 << 17;
    public static final int TABLE_LINE_ALIGN_CENTER = 1 << 18;
    public static final int TABLE_LINE_ALIGN_BOTTOM = 1 << 19;

    private int padding = 1;  //左右边距默认为1
    private Character encase = null;
    private boolean equilong = true;//默认等宽
    private int maxColWidth = 100;
    private boolean showHeader = true;
    private boolean showNo = true;
    private boolean wordWrap = true; // 自动换行
    private String sequenceName = "No."; // 自动换行

    private TableTextAlign textAlign = TableTextAlign.CENTER; // 对齐方式
    private TableLineAlign lineAlign = TableLineAlign.CENTER; // 上下对齐方式
    private TableTextAlign headerTextAlign = TableTextAlign.CENTER; // 对齐方式
    private TableLineAlign headerLineAlign = TableLineAlign.CENTER; // 上下对齐方式
    private String lineSplit = "\n";
    private Map<String, String> escapeChars = new HashMap();
    private TableBorder outside = new TableBorder('-', '|', '+');
    private TableBorder inside = new TableBorder('-', '|', '+');
    private final Splitter lineSplitter = Splitter.on(lineSplit);
    private final Splitter hexSplitter = Splitter.fixedLength(2);
    private final Joiner hexByteJoiner = Joiner.on(' ');
    private final Joiner hexLineJoiner = Joiner.on(lineSplit);


    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public TableSetting withPadding(int padding) {
        this.padding = padding;
        return this;
    }

    public TableTextAlign getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(TableTextAlign textAlign) {
        this.textAlign = textAlign;
    }

    public TableSetting withTextAlign(TableTextAlign textAlign) {
        this.textAlign = textAlign;
        return this;
    }

    public TableLineAlign getLineAlign() {
        return lineAlign;
    }

    public void setLineAlign(TableLineAlign lineAlign) {
        this.lineAlign = lineAlign;
    }

    public TableSetting withLineAlign(TableLineAlign lineAlign) {
        this.lineAlign = lineAlign;
        return this;
    }

    public TableTextAlign getHeaderTextAlign() {
        return headerTextAlign;
    }

    public void setHeaderTextAlign(TableTextAlign headerTextAlign) {
        this.headerTextAlign = headerTextAlign;
    }

    public TableSetting withHeaderTextAlign(TableTextAlign headerTextAlign) {
        this.headerTextAlign = headerTextAlign;
        return this;
    }

    public TableLineAlign getHeaderLineAlign() {
        return headerLineAlign;
    }

    public void setHeaderLineAlign(TableLineAlign headerLineAlign) {
        this.headerLineAlign = headerLineAlign;
    }

    public TableSetting withHeaderLineAlign(TableLineAlign headerLineAlign) {
        this.headerLineAlign = headerLineAlign;
        return this;
    }

    public Character getEncase() {
        return encase;
    }

    public void setEncase(Character encase) {
        this.encase = encase;
    }

    public TableSetting withEncase(Character encase) {
        this.encase = encase;
        return this;
    }

    public boolean isEquilong() {
        return equilong;
    }

    public void setEquilong(boolean equilong) {
        this.equilong = equilong;
    }

    public TableSetting withEquilong(boolean equilong) {
        this.equilong = equilong;
        return this;
    }

    public Map<String, String> getEscapeChars() {
        return escapeChars;
    }

    public void setEscapeChars(Map<String, String> escapeChars) {
        this.escapeChars = escapeChars;
    }

    public TableSetting withEscapeChars(Map<String, String> escapeChars) {
        this.escapeChars = escapeChars;
        return this;
    }

    public TableSetting appendEscapeChars(String escapeChar, String replace) {
        this.escapeChars.put(escapeChar, replace);
        return this;
    }

    public int getMaxColWidth() {
        return maxColWidth;
    }

    public void setMaxColWidth(int maxColWidth) {
        this.maxColWidth = maxColWidth;
    }

    public TableSetting withMaxColWidth(int maxColWidth) {
        this.maxColWidth = maxColWidth;
        return this;
    }

    public String getLineSplit() {
        return lineSplit;
    }

    public void setLineSplit(String lineSplit) {
        this.lineSplit = lineSplit;
    }

    public TableSetting withLineSplit(String lineSplit) {
        this.lineSplit = lineSplit;
        return this;
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    public TableSetting withShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
        return this;
    }

    public boolean isShowNo() {
        return showNo;
    }

    public void setShowNo(boolean showNo) {
        this.showNo = showNo;
    }

    public TableSetting withShowNo(boolean showNo) {
        this.showNo = showNo;
        return this;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public TableSetting withSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
        return this;
    }

    public boolean isWordWrap() {
        return wordWrap;
    }

    public void setWordWrap(boolean wordWrap) {
        this.wordWrap = wordWrap;
    }

    public TableSetting withWordWrap(boolean wordWrap) {
        this.wordWrap = wordWrap;
        return this;
    }

    public TableBorder getOutside() {
        return outside;
    }

    public void setOutside(TableBorder outside) {
        this.outside = outside;
    }

    public TableSetting withOutside(TableBorder outside) {
        this.outside = outside;
        return this;
    }

    public TableBorder getInside() {
        return inside;
    }

    public void setInside(TableBorder inside) {
        this.inside = inside;
    }

    public TableSetting withInside(TableBorder inside) {
        this.inside = inside;
        return this;
    }

    public Splitter getLineSplitter() {
        return lineSplitter;
    }

    public Splitter getHexSplitter() {
        return hexSplitter;
    }

    public Joiner getHexByteJoiner() {
        return hexByteJoiner;
    }

    public Joiner getHexLineJoiner() {
        return hexLineJoiner;
    }

    public static TableSetting build(){
        return new TableSetting();
    }

    public void initFlags(long flags) {
        if(has(flags, TableSetting.EQUILONG)){
            setEquilong(true);
        }
        if(has(flags, TableSetting.NOT_EQUILONG)){
            setEquilong(false);
        }
        if(has(flags, TableSetting.SHOW_HEADER)){
            setShowHeader(true);
        }
        if(has(flags, TableSetting.NOT_SHOW_HEADER)){
            setShowHeader(false);
        }
        if(has(flags, TableSetting.SHOW_NO)){
            setShowNo(true);
        }
        if(has(flags, TableSetting.NOT_SHOW_NO)){
            setShowNo(false);
        }
        if(has(flags, TableSetting.WORD_WRAP)){
            setWordWrap(true);
        }
        if(has(flags, TableSetting.NOT_WORD_WRAP)){
            setWordWrap(false);
        }
        if(has(flags, TableSetting.TEXT_ALIGN_LEFT)){
            setTextAlign(TableTextAlign.LEFT);
        }
        if(has(flags, TableSetting.TEXT_ALIGN_CENTER)){
            setTextAlign(TableTextAlign.CENTER);
        }
        if(has(flags, TableSetting.TEXT_ALIGN_RIGHT)){
            setTextAlign(TableTextAlign.RIGHT);
        }
        if(has(flags, TableSetting.LINE_ALIGN_TOP)){
            setLineAlign(TableLineAlign.TOP);
        }
        if(has(flags, TableSetting.LINE_ALIGN_CENTER)){
            setLineAlign(TableLineAlign.CENTER);
        }
        if(has(flags, TableSetting.LINE_ALIGN_BOTTOM)){
            setLineAlign(TableLineAlign.BOTTOM);
        }
        if(has(flags, TableSetting.TABLE_TEXT_ALIGN_LEFT)){
            setHeaderTextAlign(TableTextAlign.LEFT);
        }
        if(has(flags, TableSetting.TABLE_TEXT_ALIGN_CENTER)){
            setHeaderTextAlign(TableTextAlign.CENTER);
        }
        if(has(flags, TableSetting.TABLE_TEXT_ALIGN_RIGHT)){
            setHeaderTextAlign(TableTextAlign.RIGHT);
        }
        if(has(flags, TableSetting.TABLE_LINE_ALIGN_TOP)){
            setHeaderLineAlign(TableLineAlign.TOP);
        }
        if(has(flags, TableSetting.TABLE_LINE_ALIGN_CENTER)){
            setHeaderLineAlign(TableLineAlign.CENTER);
        }
        if(has(flags, TableSetting.TABLE_LINE_ALIGN_BOTTOM)){
            setHeaderLineAlign(TableLineAlign.BOTTOM);
        }

    }

    public boolean has(long flags, int f) {
        return (flags & f) != 0;
    }
}
