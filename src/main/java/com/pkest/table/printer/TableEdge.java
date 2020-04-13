package com.pkest.table.printer;

/**
 * @author 360733598@qq.com
 * @date 2020/4/10 11:41
 */
public class TableEdge {

    private int top = 0;
    private int bottom = 0;
    private int left = 0;
    private int rihgt = 0;

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRihgt() {
        return rihgt;
    }

    public void setRihgt(int rihgt) {
        this.rihgt = rihgt;
    }


    public TableEdge(int all) {
        this(all, all);
    }

    public TableEdge(int topBottom, int leftRight) {
        this(topBottom, topBottom, leftRight, leftRight);
    }

    public TableEdge(int top, int bottom, int left, int rihgt) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.rihgt = rihgt;
    }
}
