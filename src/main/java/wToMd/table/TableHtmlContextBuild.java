package wToMd.table;

import java.util.*;

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
        tableHtml.append(B_TABLE);
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
        cellMap.get(row).add(new Cell(cellRow, cellCol));
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
        tableHtml.append(E_TABLE);
        return tableHtml.toString();
    }

    private void buildCell() {
        cellMap.forEach((row, cellList) -> {
            tableHtml.append(B_TR);
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
                    tableHtml.append(">");
                    //数据,或许需要一定处理
                    String data = cell.getData().toString();
                    data = data.replaceAll("\n", "<br/>");
                    tableHtml.append(data);
                    //
                    tableHtml.append("</td>");
                }
            });
            tableHtml.append(E_TR);
        });
    }

    /**
     * 仅仅只有一个cell的表单
     *
     * @return
     */
    public boolean isSimpleCellTable() {
        int row = cellMap.size();
        if (row == 1) {
            List<Cell> list = cellMap.get(1);
            if (list != null && list.size() == 1)
                return true;
        }
        return false;
    }
}
