package wToMd.sax.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import wToMd.common.EventSource;
import wToMd.doc.pic.PicXmlDefine;
import wToMd.event.EventAccept;
import wToMd.event.EventSend;
import wToMd.event.EventType;
import wToMd.sax.DataAccept;

import java.util.List;

import static wToMd.doc.pic.PicXmlDefine.PIC_BLIPFILL_TAG;
import static wToMd.doc.table.TableXmlDefine.TABLE_TAG;

/**
 * <h3>wordToMd</h3>
 *
 * @param <T> 表示单次由解析器返回的数据
 * @param <U> 表示最终由handler返回给sax的数据
 * @author : ck
 * @date : 2020-05-11 16:24
 **/
public abstract class CustomHandler<T, U> extends DefaultHandler implements DataAccept<T>, EventSource {
    private StringBuilder result = new StringBuilder();
    private EventSend eventSend;
    private static Log log = LogFactory.getLog(CustomHandler.class);
    protected List<EventAccept> list;

    public abstract U getResult();

    public CustomHandler() {
        initParse();
    }

    /**
     * 子类调用
     */
    public void initParse() {
        initParseList();
        list.forEach(eventAccept -> eventAccept.changeDataAccept(this));
        eventSend = new EventSend(list, this);
    }

    //子类指定内部的解析器
    protected abstract void initParseList();
    //--------------------处理全局属性----------------------

    @Override
    public void startDocument() throws SAXException {
        log.info("解析doc开始");
    }

    //---------------------一些open xml定义的标签-----------------------

    protected String currentTag;
    protected EventType currentEvent;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentTag = qName;
        EventType eventType = null;
        if (qName.equals(TABLE_TAG)) {//表单
            eventType = EventType.TABLEBEGIN;
        } else if (qName.equals(PIC_BLIPFILL_TAG)) {//图片
            eventType = EventType.PICBEGIN;
        }
        fireEvent(eventType);
        eventSend.dealStartEle(uri, localName, qName, attributes, result);
    }

    private void fireEvent(EventType eventType) {
        currentEvent = eventType;
        eventSend.sendAll(eventType);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        eventSend.dealEndEle(uri, localName, qName);
        //当标签结束,则将所有解析器数据源还原给当前
        if (qName.equals(PIC_BLIPFILL_TAG)) { //图片处理结束
            eventSend.sendAll(EventType.PICEND);
        }
        list.stream().filter(eventAccept -> eventAccept.support(currentEvent)).forEach(eventAccept -> eventAccept.changeDataAccept(this));
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String text = new String(ch, start, length);
        if (text.contains("\n") || text.contains("\r"))
            return;
        eventSend.sendData(currentTag, text);
    }
    //----------------------事件------------------------

    @Override
    public List<EventAccept> getAllEventAccept() {
        return list;
    }
}
