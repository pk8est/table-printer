package com.pkest.table.printer;

/**
 * @author 360733598@qq.com
 * @date 2020/4/10 12:29
 */

@FunctionalInterface
public interface TableConsumer<T, E extends Exception> {
    void accept(T t) throws E;
}