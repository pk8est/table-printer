package com.pkest.table.printer;

/**
 * @author 360733598@qq.com
 * @date 2020/4/10 11:41
 */
public class TableBorder {

    private Character lineSplitter = '-';//默认水平分隔符
    private Character rowSplitter = '|'; //默认竖直分隔符
    private Character crossSplitter = '+';//默认交叉分隔符

    public Character getLineSplitter() {
        return lineSplitter;
    }

    public void setLineSplitter(Character lineSplitter) {
        this.lineSplitter = lineSplitter;
    }

    public TableBorder withLineSplitter(Character lineSplitter) {
        this.lineSplitter = lineSplitter;
        return this;
    }

    public Character getRowSplitter() {
        return rowSplitter;
    }

    public void setRowSplitter(Character rowSplitter) {
        this.rowSplitter = rowSplitter;
    }

    public TableBorder withRowSplitter(Character rowSplitter) {
        this.rowSplitter = rowSplitter;
        return this;
    }

    public Character getCrossSplitter() {
        return crossSplitter;
    }

    public void setCrossSplitter(Character crossSplitter) {
        this.crossSplitter = crossSplitter;
    }

    public TableBorder withCrossSplitter(Character crossSplitter) {
        this.crossSplitter = crossSplitter;
        return this;
    }

    public TableBorder(Character lineSplitter, Character rowSplitter, Character crossSplitter) {
        this.lineSplitter = lineSplitter;
        this.rowSplitter = rowSplitter;
        this.crossSplitter = crossSplitter;
    }
}
