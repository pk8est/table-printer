/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pkest.table.printer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.fusesource.jansi.AnsiString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Strings.repeat;
import static com.google.common.collect.Iterables.partition;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.io.BaseEncoding.base16;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static jline.console.WCWidth.wcwidth;

public class TablePrinter {

    private final static Logger logger = LoggerFactory.getLogger(TablePrinter.class);

    public final static TablePrinter DEFAULT = TablePrinter.build(TableSetting.build().withShowNo(false));
    public final static TablePrinter FULL = TablePrinter.build(TableSetting.build());
    public final static TablePrinter SIMPLE = TablePrinter.build(TableSetting.build()
            .withShowHeader(false)
            .withOutside(null)
            .withShowNo(false));
    public final static TablePrinter CSV = TablePrinter.build(TableSetting.build()
            .withShowNo(false)
            .withPadding(null)
            .withLineSplit(null)
            .withMaxColWidth(-1)
            .appendEscapeChars("\n", "\\\\n")
            .appendEscapeChars("\r", "\\\\r")
            .withTextAlign(TableTextAlign.LEFT)
            .withEquilong(false)
            .withInside(new TableBorder(null, ',', null))
            .withOutside(null));
    public final static TablePrinter CSV2 = TablePrinter.build(CSV.copySetting().withEncase('"'));

    private final TableSetting setting;

    public TableSetting copySetting() {
        TableSetting setting = new TableSetting();
        try {
            BeanUtils.copyProperties(setting, this.setting);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return setting;
    }

    public static TablePrinter build(TableSetting setting){
        return new TablePrinter(setting);
    }

    public TablePrinter(TableSetting setting){
        this.setting = setting;
    }

    public Writer dump(Object rawData){
        return dump(rawData, new StringWriter());
    }

    public Writer dump(Object rawData, Writer writer){
        return render(rawData, writer, TableSetting.NOT_SHOW_HEADER | TableSetting.NOT_SHOW_NO);
    }

    public Writer render(Object rawData){
        return render(rawData, new StringWriter());
    }

    public Writer render(Object rawData, Writer writer){
        return render(rawData, null, writer);
    }

    public Writer render(Object rawData, List headers){
        return render(rawData, headers, new StringWriter());
    }

    public Writer render(Object rawData, long flags){
        return render(rawData, new StringWriter(), flags);
    }

    public Writer render(Object rawData, TableSetting setting){
        return render(rawData, null, new StringWriter(), 0, setting);
    }

    public Writer render(Object rawData, List headers, long flags){
        return render(rawData, headers, new StringWriter(), flags, this.setting);
    }

    public Writer render(Object rawData, List headers, Writer writer){
        return render(rawData, headers, writer, 0, this.setting);
    }

    public Writer render(Object rawData, Writer writer, long flags){
        return render(rawData, null, writer, flags, this.setting);
    }

    public Writer render(Object rawData, List headers, Writer writer, long flags, TableSetting setting){
        List<List> rows = Lists.newArrayList();
        try {
            List data =transformData(rawData);
            setting.initFlags(flags);
            if(data != null && data.size() > 0){
                if(headers == null) headers = initHeader(data.get(0));
                rows = initData(data, headers, setting);
            }else if(headers == null){
                headers = Lists.newArrayList(1);
            }
            if(setting.isShowNo()){
                headers.add(0, setting.getSequenceName());
            }
            Integer[] maxWidth = mathMaxWidth(rows, headers, setting);

            if(setting.getMargin() != null){
                writer.append(repeat("\r\n", setting.getMargin().getTop()));
            }

            writeLineWithNotBlank(formatBorder(maxWidth, setting.getOutside(), setting.getOutside(), setting), writer::append);
            if(setting.isShowHeader()){
                renderRow(headers, headers, maxWidth, setting.getHeaderTextAlign(), setting.getHeaderLineAlign(), writer, setting);
                writeLineWithNotBlank(formatBorder(maxWidth, setting.getOutside(), setting.getInside(), setting), writer::append);
            }
            renderBody(rows, headers, maxWidth, writer, setting);

            if(setting.getMargin() != null){
                writer.append(repeat("\r\n", setting.getMargin().getBottom()));
            }

        }catch (IOException e){
            logger.error("", e);
            throw new RuntimeException(e);
        }

        return writer;
    }

    protected void renderBody(List<List> rows, List headers, Integer[] maxWidth, Writer writer, TableSetting setting)throws IOException{
        int rowIndex = 0;
        for (List row : rows) {
            renderRow(row, headers, maxWidth, setting.getTextAlign(), setting.getLineAlign(), writer, setting);
            if(rowIndex < rows.size()-1){
                writeLineWithNotBlank(formatBorder(maxWidth, setting.getOutside(), setting.getInside(), setting), writer::append);
            }
            rowIndex++;
        }

        writer.write(formatBorder(maxWidth, setting.getOutside(), setting.getOutside(), setting));
        writer.append('\n');
    }

    protected void renderRow(List row, List headers, Integer[] maxWidth, TableTextAlign textAlign, TableLineAlign lineAlign, Writer writer, TableSetting setting)throws IOException{
        int columns = headers.size();
        List<List<String>> columnLines = new ArrayList<>(columns);
        int maxLines = 1;
        TableEdge padding = setting.getPadding() == null ? new TableEdge(0) : setting.getPadding();
        TableEdge margin = setting.getMargin() == null ? new TableEdge(0) : setting.getMargin();
        int leftPadding = padding.getLeft();
        int rightPadding = padding.getRihgt();
        String leftMargin = repeat(" ", margin.getLeft());
        String rightMargin = repeat(" ", margin.getRihgt());
        List<String> topPadding = Stream.iterate(0, i->i+1).limit(padding.getTop())
                .map(e -> "").collect(Collectors.toList());
        List<String> bottomPadding = Stream.iterate(0, i->i+1).limit(padding.getBottom())
                .map(e -> "").collect(Collectors.toList());

        for (int i = 0; i < columns; i++) {
            List<String> lines = wordWrapValue(formatValue(row.get(i), setting.getEncase(), setting), setting);
            lines.addAll(0, topPadding);
            lines.addAll(bottomPadding);
            columnLines.add(lines);
            maxLines = max(maxLines, lines.size());
        }

        for (int line = 0; line < maxLines; line++) {
            writer.append(leftMargin);
            writerBorderSand(setting.getOutside(), setting.getInside(), TableBorder::getRowSplitter, writer::append);
            for (int column = 0; column < columns; column++) {
                if(column > 0) {
                    writerBorderSand(setting.getInside(), setting.getOutside(), TableBorder::getRowSplitter, writer::append);
                }
                List<String> lines = columnLines.get(column);
                int diff = 0;
                switch (lineAlign){
                    case BOTTOM:
                        diff = maxLines - lines.size();
                        break;
                    case CENTER:
                        diff = (maxLines - lines.size()) / 2;
                        break;
                }
                String s = line >= diff && (line - diff < lines.size()) ? lines.get(line - diff) : "";
                String out = align(s, maxWidth[column], leftPadding, rightPadding, textAlign);
                writer.append(out);
            }
            writerBorderSand(setting.getOutside(), setting.getInside(), TableBorder::getRowSplitter, writer::append);
            writer.append(rightMargin);
            writer.append('\n');

        }
    }


    protected List initHeader(Object row){
        List headers;
        if(row instanceof String[]){
            headers = Arrays.asList((String[])row);
        }else if (row instanceof Collection) {
            headers = Stream.iterate(1, i->i+1).limit(((Collection) row).size())
                    .map(String::valueOf).collect(Collectors.toList());
        }else if(row instanceof Map){
            headers = (List<String>) ((Map) row).keySet().stream().collect(Collectors.toList());
        }else if(row == null || row instanceof String || TableUtils.isWrapClass(row.getClass())){
            headers = Lists.newArrayList(1);
        }else{
            headers = new BeanMap(row).keySet().stream().collect(Collectors.toList());
        }

        return headers;
    }

    protected List<List> transformData(Object rawData) {
        List data;
        if(rawData == null) {
            data = Lists.newArrayList(rawData);
        }else if(rawData instanceof List){
            data = (List) rawData;
        }else if(rawData instanceof Map){
            data = ((Map<?, ?>) rawData).entrySet().stream()
                    .map(e -> Lists.newArrayList(e.getKey(), e.getValue()))
                    //.flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }else if(!(rawData instanceof String) && !TableUtils.isWrapClass(rawData.getClass())){
            Map<?, ?> map = new BeanMap(rawData);
            data = map.entrySet().stream()
                    .map(e -> Lists.newArrayList(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
        }else{
            data = Lists.newArrayList(rawData);
        }
        return data;
    }

    protected List<List> initData(List data, List headers, TableSetting setting) {
        final int[] index = {0};
        return (List<List>) data.stream().map(item -> initData(item, headers, index[0]++, setting)).collect(Collectors.toList());
    }

    private List initData(Object row, List headers, int rowIndex, TableSetting setting) {
        List data = Lists.newArrayList();
        if(row instanceof String[]){
            data.addAll(Arrays.asList((String[])row));
        } else if (row instanceof List) {
            if(((List) row).size() > headers.size()){
                data.addAll((Collection) headers.stream()
                        .map(e -> ((List) row).get(Integer.valueOf(e.toString())))
                        .collect(Collectors.toList()));
            }else{
                data.addAll((Collection) row);
            }
        } else if(row instanceof Map){
            Map map = (Map) row;
            data.addAll(mapToCollection(map, headers));
        }else if(row == null || row instanceof String || TableUtils.isWrapClass(row.getClass())){
            data.add(row);
        }else{
            data.addAll(mapToCollection(new BeanMap(row), headers));
        }
        if(setting.isShowNo()){
            data.add(0, rowIndex + 1);
        }
        return data;
    }

    protected Collection mapToCollection(Map map, List headers){
        return (Collection) headers.stream()
                .map(e -> map.containsKey(e) ? map.get(e) : null)
                .collect(Collectors.toList());
    }

    protected List wordWrapValue(String str, TableSetting setting){
        if(setting.getLineSplit() == null){
            return Lists.newArrayList(str);
        }
        return ImmutableList.copyOf(setting.getLineSplitter().split(str))
                .stream()
                .map(s -> {
                    if(setting.getMaxColWidth() > 0 && s.length() > setting.getMaxColWidth()){
                        if(setting.isWordWrap()){
                            return TableUtils.splitStringByLength(s, setting.getMaxColWidth());
                        }else{
                            return Lists.newArrayList(s.substring(0, setting.getMaxColWidth()) + "...");
                        }
                    }
                    return Lists.newArrayList(s);
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    protected Integer[] mathMaxWidth(List<List> rows, List headers, TableSetting setting){
        Integer[] maxWidth = new Integer[headers.size()];
        for (int i = 0, len = headers.size(); i < len; i++) {
            maxWidth[i] = setting.isEquilong() ? max(1, setting.isShowHeader() ? consoleWidth(headers.get(i).toString()) : 1) : -1;
        }
        if(rows != null && rows.size() > 0){
            for (List<?> row : rows) {
                for (int i = 0; i < headers.size(); i++) {
                    String s = formatValue(row.get(i), setting.getEncase(), setting);
                    maxWidth[i] = setting.isEquilong() ? max(maxWidth[i], maxLineLength(s, setting)) : -1;
                }
            }
        }
        if(setting.getMaxColWidth() > 0){
            for (int i = 0; i < maxWidth.length; i++) {
                maxWidth[i] = min(maxWidth[i], setting.getMaxColWidth());
            }
        }
        return maxWidth;
    }

    protected void writeLineWithNotBlank(String data, TableConsumer<String, IOException> writer) throws IOException {
        if(StringUtils.isNotBlank(data)){
            writer.accept(data);
            writer.accept("\n");
        }
    }

    protected String formatBorder(Integer[] maxWidth, TableBorder outside, TableBorder inside, TableSetting setting) throws IOException {
        StringBuilder writer = new StringBuilder();

        writerBorderSand(outside, inside, TableBorder::getCrossSplitter, writer::append);

        int index = 0;
        for (int width: maxWidth){
            if(index++ > 0) {
                writerBorderSand(inside, outside, TableBorder::getCrossSplitter, writer::append);
            }
            writerBorderSand(inside, outside, TableBorder::getLineSplitter, s -> {
                writer.append(repeat(String.valueOf(s), (width == -1 ? 1 : width)
                        + setting.getPadding().getLeft() + setting.getPadding().getRihgt()));
            });
        }

        writerBorderSand(outside, inside, TableBorder::getCrossSplitter, writer::append);

        String output = writer.toString();
        if(StringUtils.isNotBlank(output) && setting.getMargin() != null){
            return repeat(" ", setting.getMargin().getLeft())
                    + output
                    + repeat(" ", setting.getMargin().getRihgt());
        }
        return output;

    }

    protected void writerBorderSand(TableBorder currect, TableBorder compare,
                                    Function<TableBorder, Object> get,
                                    TableConsumer<String, IOException> writer
    ) throws IOException {
        writerBorderSand(currect, compare, get, writer, " ");
    }

    protected void writerBorderSand(TableBorder currect, TableBorder compare,
                                    Function<TableBorder, Object> get,
                                    TableConsumer<String, IOException> writer,
                                    String defaultChar
    ) throws IOException {
        if(currect != null) {
            Object c = get.apply(currect);
            if(c != null){
                writer.accept(c.toString());
            }
        } else if(currect == null && compare != null) {
            writer.accept(defaultChar);
        }
    }

    protected String formatValue(Object o, Character encase, TableSetting setting)
    {
        if (o == null) {
            return "NULL";
        }

        if (o instanceof byte[]) {
            o = formatHexDump((byte[]) o, 16, setting);
        }

        if(encase != null){
            o = encase + TableUtils.escape(o.toString(), String.valueOf(encase), "\\"+encase) + encase;
        }
        for (Map.Entry<String, String> character: setting.getEscapeChars().entrySet()){
            o = TableUtils.escape(o.toString(), character.getKey(), character.getValue());
        }

        return o.toString();
    }

    protected String formatHexDump(byte[] bytes, int bytesPerLine, TableSetting setting){
        Iterable<String> hexPairs = createHexPairs(bytes, setting);
        Iterable<List<String>> hexLines = partition(hexPairs, bytesPerLine);
        Iterable<String> lines = transform(hexLines, setting.getHexByteJoiner()::join);
        return setting.getHexLineJoiner().join(lines);
    }


    protected Iterable<String> createHexPairs(byte[] bytes, TableSetting setting){
        String hexDump = base16().lowerCase().encode(bytes);
        return setting.getHexSplitter().split(hexDump);
    }

    protected String align(String s, int maxWidth, int leftPadding, int rightPadding, TableTextAlign align){
        if(maxWidth < 0){
            maxWidth = s.length();
        }
        switch (align){
            case LEFT:
                return align(s, maxWidth, leftPadding, rightPadding, false);
            case RIGHT:
                return align(s, maxWidth, leftPadding, rightPadding, true);
            default:
                return center(s, maxWidth, leftPadding, rightPadding);
        }
    }

    protected String center(String s, int maxWidth, int leftPadding, int rightPadding){
        int width = consoleWidth(s);
        int left = (maxWidth - width) / 2;
        int right = maxWidth - (left + width);
        return repeat(" ", left + leftPadding) + s + repeat(" ", right + rightPadding);
    }

    protected String align(String s, int maxWidth, int leftPadding, int rightPadding, boolean right){
        int width = consoleWidth(s);
        String large = maxWidth - width > 0 ? repeat(" ", (maxWidth - width)) : "";
        return repeat(" ", leftPadding)
                + (right ? (large + s) : (s + large))
                + repeat(" ", rightPadding);
    }

    protected int maxLineLength(String s, TableSetting setting){
        int n = 0;
        for (String line : setting.getLineSplitter().split(s)) {
            n = max(n, consoleWidth(line));
        }
        return n;
    }

    protected int consoleWidth(String s){
        return consoleWidth(new AnsiString(s));
    }

    protected int consoleWidth(AnsiString s){
        CharSequence plain = s.getPlain();
        int n = 0;
        for (int i = 0; i < plain.length(); i++) {
            n += max(wcwidth(plain.charAt(i)), 0);
        }
        return n;
    }

}
