package wToMd.doc.table;

import wToMd.common.CommonSymbol;
import wToMd.common.ContextBuild;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-11 14:53
 **/
public class TableHtmlContextBuild implements ContextBuild<String> {
    private StringBuilder tableHtml = new StringBuilder();
    //用来存在出现了列合并的cell,直到出现  <w:vMerge/>,表示合并结束
    private Map<Integer, Cell> rowMergeCellMap = new HashMap();
    private Map<Integer, List<Cell>> cellMap = new LinkedHashMap();
    //xml标签
    public static final String B_TABLE = "<table>";
    public static final String E_TABLE = "</table>";
    public static final String B_TR = "<tr>";
    public static final String E_TR = "</tr>";

    public TableHtmlContextBuild() {
        tableHtml.append(B_TABLE).append(CommonSymbol.commonLine);
    }

    /**
     * @param row
     * @param col
     */
    public TableHtmlContextBuild(int row, int col) {
        this();
    }

    /**
     * 创建一个新的cell
     *
     * @param row
     */
    public void insertCell(int row, int cellRow, int cellCol) {
        if (cellMap.get(row) == null) {
            cellMap.put(row, new ArrayList());
        }
        Cell cell = new Cell(cellRow, cellCol);
        cellMap.get(row).add(cell);
    }

    /**
     * 获取当前正在处理的cell
     *
     * @param row
     * @return
     */
    public Cell getInsertingCell(int row) {
        List<Cell> list = this.cellMap.get(row);
        return list.get(list.size() - 1);
    }

    /**
     * @param col
     * @param cell
     */
    public void addToMergeCell(int col, Cell cell) {
        this.rowMergeCellMap.put(col, cell);
    }

    public Cell getMergeCell(int col) {
        return this.rowMergeCellMap.get(col);
    }

    /**
     * 结束构建
     *
     * @return
     */
    public String build() {
        buildCell();
        tableHtml.append(E_TABLE).append(CommonSymbol.commonLine);
        return tableHtml.toString();
    }

    private void buildCell() {
        cellMap.forEach((row, cellList) -> {
            tableHtml.append(B_TR).append(CommonSymbol.commonLine);
            cellList.forEach(cell -> {
                if (!cell.isRowMerged()) {
                    int rowMerge = cell.getRowMerge();
                    int colMerge = cell.getColsMerge();
                    tableHtml.append("<td");
                    if (rowMerge != 0) {
                        tableHtml.append(" rowspan=" + rowMerge);
                    } else if (colMerge != 0) {
                        tableHtml.append(" colspan=" + colMerge);
                    }
                    tableHtml.append(">").append(CommonSymbol.commonLine);
                    //数据,或许需要一定处理
                    String data = cell.getData().toString();
//                    data = data.replaceAll("\n", "<br/>");
                    tableHtml.append(data);
                    //
                    tableHtml.append("</td>").append(CommonSymbol.commonLine);
                }
            });
            tableHtml.append(E_TR).append(CommonSymbol.commonLine);
        });
    }

    /**
     * 仅仅为单列的表单
     *
     * @return
     */
    public boolean isSimpleCellTable() {
        int row = cellMap.size();
        if (row == 1) {
            return true;
        }
        return false;
    }

    /**
     * 失败构造
     *
     * @return
     */
    public String buildFailure() {
        tableHtml = new StringBuilder();
        cellMap.forEach((row, cellList) -> cellList.forEach(cell -> tableHtml.append(cell.getData())));
        String outStr = tableHtml.toString();
        StringBuilder stringBuilder = new StringBuilder();
        //将outStr使用
        String picRex = "!\\[.*?\\]\\(.*? \".*?\"\\)";
        outStr = outStr.replaceAll("</br>", CommonSymbol.commonLine);
        String[] out = outStr.split(picRex);
        if (out.length > 1) {//分部分处理
            stringBuilder.append(regexMatchPicStr(outStr, picRex));
        } else {
            stringBuilder.append("```").append(CommonSymbol.commonLine).append(out[0]).append("```").append(CommonSymbol.commonLine);
        }
        return stringBuilder.toString();
    }

    private String regexMatchPicStr(String src, String regex) {
        StringBuilder stringBuilder = new StringBuilder();
        Matcher matcher = Pattern.compile(regex).matcher(src);
        int startIndex = 0;
        List<String> list = new ArrayList();
        int extraCharCount = 0;
        while (matcher.find(startIndex)) {
            String target = matcher.group();
            list.add(target);
            int targetStartIndex = matcher.start();
            int targetEndIndex = matcher.end();
            //添加正则匹配前的数据
            String temp = src.substring(startIndex, targetStartIndex==0?0: targetStartIndex - 1);
            if (temp != null && !temp.equals(""))
                appendCodeSymbol(stringBuilder, temp);
            //添加正则数据
            stringBuilder.append(target).append(CommonSymbol.commonLine);
            startIndex = targetEndIndex;
        }

        String temp = src.substring(startIndex + extraCharCount);
        if (temp != null && !temp.equals(""))
            appendCodeSymbol(stringBuilder, temp);
        return stringBuilder.toString();
    }

//    private final int len = 6 + CommonSymbol.commonLine.length();

    private void appendCodeSymbol(StringBuilder stringBuilder, String str) {
        stringBuilder.append("```").append(CommonSymbol.commonLine).append(str).append("```").append(CommonSymbol.commonLine);
//        return len;
    }
}
