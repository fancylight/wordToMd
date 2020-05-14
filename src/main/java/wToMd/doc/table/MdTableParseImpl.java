package wToMd.doc.table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import wToMd.common.AbstractParse;
import wToMd.common.CommonSymbol;
import wToMd.doc.pic.MdPicParseImpl;
import wToMd.doc.title.MdParagraphParseImpl;
import wToMd.event.EventType;


/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-11 16:11
 **/
public class MdTableParseImpl extends AbstractParse<TableHtmlContextBuild> {
    Log log = LogFactory.getLog(MdTableParseImpl.class);

    public MdTableParseImpl() {
    }

    public MdTableParseImpl(boolean isInner) {
        super(isInner);
    }

    public MdTableParseImpl(Class<TableHtmlContextBuild> contextBuildClass, boolean isInner) {
        super(contextBuildClass, isInner);
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
        String htmlTable;
        if (!cellFailure)
            htmlTable = contextBuild.build();
        else {
            htmlTable = this.contextBuild.buildFailure();
        }
        clear();
        return htmlTable + CommonSymbol.commonLine;
    }

    @Override
    protected TableHtmlContextBuild newBuild() {
        contextBuild = super.newBuild();
        if (contextBuild == null) {
            contextBuild = new TableHtmlContextBuild();
        }
        return contextBuild;
    }

    /**
     * 清除状态
     */
    @Override
    protected void clear() {
        contextBuild = null;
        rowCount = 0;
        colCount = 0;
        cellFailure = false;
    }

    private int rowCount;
    private int colCount;

    /**
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     */
    public void dealStartEle(String uri, String localName, String qName, Attributes attributes) {
        if (!support(true))
            return;
        if (qName.equals(TableXmlDefine.TABLE_TAG)) {//表单出现
            newBuild();
        }
        if (qName.equals(TableXmlDefine.TR_TAG)) { //出现了行
            rowCount++;
            colCount = 0;
            return;
        }
        if (qName.equals(TableXmlDefine.TC_TAG)) { //新的cell,即列增加
            colCount++;
            this.contextBuild.insertCell(rowCount, rowCount, colCount);
            log.info("创建 行" + rowCount + "列" + colCount + " cell");
            return;
        }
        if (qName.equals(TableXmlDefine.V_MERGE)) {//处理列合并
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
        if (qName.equals(TableXmlDefine.GRID_SPAN)) {//出现了行合并情况
            Cell cell = this.contextBuild.getInsertingCell(rowCount);
            cell.setColsMerge(Integer.valueOf(attributes.getValue("w:val")));
            return;
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
        if (!support(false))
            return;
        if (qName.equals(TableXmlDefine.TABLE_TAG)) {//表达结束,尝试构建数据
            //判断是否为一行一列,如果满足条件,那么说明这tm不是表格(sber玩意)
            if (this.contextBuild.isSimpleCellTable()) {//放弃
                cellFailure = true;
            }
            sendData();
        }
    }

    /**
     * @param tag
     * @param context
     */
    @Override
    public void dealText(String tag, String context) {
        if (!support(true))
            return;
    }

    private boolean cellFailure = false;

    /**
     * 基本出现在tc内部,也就是说这部分数据要合并到{@link Cell#getData()}部分
     * <p>
     * 若发生了内嵌,那么说明该table不仅仅当作table使用了,目前仅仅考虑被内部镶嵌了图片
     * 当table结束后,就要不进行table构建过程,而是使用特别的构建方式
     *
     * @param data
     */
    @Override
    public void acceptData(Object data, AbstractParse abstractParse) {
        Cell cell = this.contextBuild.getInsertingCell(rowCount);
        if (abstractParse instanceof MdPicParseImpl) {
            cellFailure = true;
            cell.getData().append(CommonSymbol.commonLine).append(data);
        } else if (abstractParse instanceof MdParagraphParseImpl) {
            //将p数据插入到当前cell的data中,把其中换行符号替换成</br>
            cell.getData().append(((String) data).replaceAll(CommonSymbol.commonLine, "</br>"));
        }
    }
}
