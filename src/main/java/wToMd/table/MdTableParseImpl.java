package wToMd.table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import wToMd.common.CommonDefine;
import wToMd.event.EventType;
import wToMd.sax.DataAccept;

import static wToMd.table.TableXmlDefine.*;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-11 16:11
 **/
public class MdTableParseImpl extends Parse<TableHtmlContextBuild> {
    Log log = LogFactory.getLog(MdTableParseImpl.class);

    public MdTableParseImpl() {
        initEvent();
    }

    public MdTableParseImpl(DataAccept dataAccept, Class<TableHtmlContextBuild> contextBuildClass) {
        super(contextBuildClass);
    }

    protected void initEvent() {
        super.acceptEvent = new EventType[]{EventType.TABLEBEGIN, EventType.TABLEEND};
    }

    /**
     * 获取数据
     *
     * @return
     */
    public String buildTableResult() {
        return getTableHtml();
    }

    @Override
    protected ContextBuild newBuild() {
        contextBuild = (TableHtmlContextBuild) super.newBuild();
        if (contextBuild == null) {
            contextBuild = new TableHtmlContextBuild();
        }
        return contextBuild;
    }

    /**
     * 返回html数据
     *
     * @return
     */
    private String getTableHtml() {
        String htmlTable = contextBuild.build();
        clear();
        return htmlTable;
    }

    /**
     * 清除状态
     */
    @Override
    protected void clear() {
        contextBuild = null;
        rowCount = 0;
        colCount = 0;
        dealSimpleCell = false;
        pCount = 0;
    }

    private int rowCount;
    private int colCount;
    private boolean dealSimpleCell;
    private int pCount;

    /**
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     */
    public void dealStartEle(String uri, String localName, String qName, Attributes attributes) {
        if (!support() && isWork)
            return;
        if (qName.equals(TABLE_TAG)) {//表单出现
            newBuild();
        }
        if (qName.equals(TR_TAG)) { //出现了行
            rowCount++;
            colCount = 0;
            return;
        }
        if (qName.equals(TC_TAG)) { //新的cell,即列增加
            dealSimpleCell = true;
            colCount++;
            pCount = 0;
            this.contextBuild.insertCell(rowCount, rowCount, colCount);
            log.info("创建 行" + rowCount + "列" + colCount + " cell");
            return;
        }
        if (qName.equals(V_MERGE)) {//处理列合并
            String restart = attributes.getValue("w:val");
            if (restart == null || restart.equals("continue")) { //说明该列是一个被合并表格,实际上不会被显示
                Cell cell = this.contextBuild.getMergeCell(colCount);
                cell.setRowMerge(rowCount - cell.getRow() + 1);
                Cell cell1 = this.contextBuild.getInsertingCell(rowCount);
                cell1.setRowMerged(true);
            } else { //该列是一个行合并cell
                Cell cell = this.contextBuild.getInsertingCell(rowCount);
                this.contextBuild.addToMergeCell(cell.getCol(), cell);
            }
            return;
        }
        if (qName.equals(GRID_SPAN)) {//出现了行合并情况
            Cell cell = this.contextBuild.getInsertingCell(rowCount);
            cell.setColsMerge(Integer.valueOf(attributes.getValue("w:val")));
            return;
        }
        if (dealSimpleCell) {
            Cell cell = this.contextBuild.getInsertingCell(rowCount);
            if (qName.equals(CommonDefine.P)) {//出现一行
                pCount++;
                if (pCount > 1) {
                    cell.getData().append("</br>");
                }
                return;
            }
        }
    }

    /**
     * 处理结束
     *
     * @param uri
     * @param localName
     * @param qName
     */
    @Override
    public void endEle(String uri, String localName, String qName) {
        if (!support() && isWork)
            return;
        if (qName.equals(TC_TAG)) {
            dealSimpleCell = false;
        }
        if (qName.equals(TABLE_TAG)) {//表达结束,尝试构建数据
            //判断是否为一行一列,如果满足条件,那么说明这tm不是表格(sber玩意)
            if (this.contextBuild.isSimpleCellTable()) {//放弃
                clear();
                return;
            }
            String re = buildTableResult();
            isWork = false;
            dataAccept.acceptData(re);
        }
    }

    /**
     * @param tag
     * @param context
     */
    @Override
    public void dealText(String tag, String context) {
        if (!support() && isWork)
            return;
        if (dealSimpleCell) {
            if (tag.equals(CommonDefine.T)) {
                Cell cell = this.contextBuild.getInsertingCell(rowCount);
                cell.getData().append(context);
            }
        }
    }

    private boolean isWork = false;

    @Override
    public boolean support() {
        boolean eventSupport = super.support();
        if (super.currentEvent == EventType.TABLEBEGIN)
            isWork = true;
        return eventSupport;
    }
}