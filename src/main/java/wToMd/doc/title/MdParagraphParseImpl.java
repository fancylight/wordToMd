package wToMd.doc.title;

import org.xml.sax.Attributes;
import wToMd.common.AbstractParse;
import wToMd.doc.pic.MdPicParseImpl;
import wToMd.event.EventType;

import static wToMd.doc.title.ParagraphDefine.*;
import static wToMd.event.EventType.*;


/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-14 14:40
 **/
public class MdParagraphParseImpl extends AbstractParse<MdParagraphContextBuild> {
    @Override
    protected void initEvent() {
        this.acceptEvent = new EventType[]{PBEGIN, PENG};
    }

    @Override
    public Object buildTableResult() {
        return contextBuild.build();
    }

    @Override
    protected MdParagraphContextBuild newBuild() {
        contextBuild = super.newBuild();
        if (contextBuild == null)
            contextBuild = new MdParagraphContextBuild();
        return contextBuild;
    }

    private int nextLevel = 0;

    /**
     * 在open xml中实际能够表示9级标题,但是一般不会使用这么多,不采用
     * <p>
     * 在docx的xml文件/word/style.xml中都是按照顺序列出来的引用,其中标题由2-10表示
     */
    private String[] paragraphMdStr = new String[]{"", "", "# ", "## ", "### ", "#### ", "##### ", "###### "};

    @Override
    public void dealStartEle(String uri, String localName, String qName, Attributes attributes) {
        if (!support(true)) {
            return;
        }
        if (qName.equals(P_TAG)) {//出现段落
            newBuild();
            nextLevel = 0;
        } else if (qName.equals(P_STYLE)) {//说明这个段落是一个标题
            int tempLevel = Integer.parseInt(attributes.getValue("w:val"));
            if (tempLevel <= 7) { //超过说明不是标题,或者超过了6级标题,不处理
                nextLevel = tempLevel;
            }
        }
    }

    @Override
    public void endEle(String uri, String localName, String qName) {
        if (!support(false)) {
            return;
        }
        //添加一个换行
        contextBuild.endWithLine();
        //
        sendData();
    }

    @Override
    public void dealText(String tag, String context) {
        if (!support(true))
            return;
        if (tag.equals(T_TAG)) {
            contextBuild.insertNewStrByT(paragraphMdStr[nextLevel] + context);
        }
    }

    @Override
    public void acceptData(Object data, AbstractParse abstractParse) {
        if (abstractParse instanceof MdPicParseImpl) {
            contextBuild.insertNewStrByT((String) data);
        }
    }
}
