package wToMd.doc.table;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-11 17:30
 **/
public class Cell {
    private int row;
    private int col;
    private int colsMerge;
    private int rowMerge;
    private StringBuffer data = new StringBuffer();
    private boolean isRowMerged;

    public int getColsMerge() {
        return colsMerge;
    }

    public void setColsMerge(int colsMerge) {
        this.colsMerge = colsMerge;
    }

    public int getRowMerge() {
        return rowMerge;
    }

    public void setRowMerge(int rowMerge) {
        this.rowMerge = rowMerge;
    }

    public StringBuffer getData() {
        return data;
    }

    public void setData(StringBuffer data) {
        this.data = data;
    }

    public boolean isRowMerged() {
        return isRowMerged;
    }

    public void setRowMerged(boolean rowMerged) {
        isRowMerged = rowMerged;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
