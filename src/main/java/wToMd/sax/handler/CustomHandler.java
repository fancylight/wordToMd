package wToMd.sax.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import wToMd.common.EventSource;
import wToMd.event.EventAccept;
import wToMd.event.EventSend;
import wToMd.event.EventType;
import wToMd.sax.DataAccept;

import java.util.List;
import java.util.stream.Collectors;

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
        EventType eventType = currentEvent;

        if (qName.equals(TABLE_TAG)) {//表单
            eventType = EventType.TABLEBEGIN;
        } else if (qName.equals(PIC_BLIPFILL_TAG)) {//图片
            eventType = EventType.PICBEGIN;
        }
        fireBeginEvent(eventType);
        eventSend.dealStartEle(uri, localName, qName, attributes, result);
    }

    private void fireBeginEvent(EventType eventType) {
        //判断是否出现了内嵌情况
        if (eventType != currentEvent && eventType != EventType.COMMON) {
            if ((eventType.isBegin() && currentEvent.isBegin())) { //上一个事件未完成,即内嵌
                //获取处理
                changeAccept(eventType, currentEvent);
            }
        }
        currentEvent = eventType;
        eventSend.sendAll(eventType);
    }

    /**
     * 将处理器数据导向之前的处理器,完成内嵌功能
     *
     * @param eventType
     * @param currentEvent
     */
    protected void changeAccept(EventType eventType, EventType currentEvent) {
        List<EventAccept> nowParse = list.stream().filter(eventAccept -> eventAccept.support(currentEvent)).collect(Collectors.toList());
        List<EventAccept> innerParse = list.stream().filter(eventAccept -> eventAccept.support(eventType)).collect(Collectors.toList());
        innerParse.forEach(eventAccept -> nowParse.forEach(eventAccept1 -> eventAccept.changeDataAccept(eventAccept1)));
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        //当标签结束,则将所有解析器数据源还原给当前
        currentTag = qName;
        EventType eventType = currentEvent;
        if (qName.equals(PIC_BLIPFILL_TAG)) { //图片处理结束
            eventType = EventType.PICEND;
        } else if (qName.equals(TABLE_TAG)) {//表格处理结束
            eventType = EventType.TABLEEND;
        }
        fireEndEvent(eventType);
        eventSend.dealEndEle(uri, localName, qName);
        list.forEach(eventAccept -> eventAccept.changeDataAccept(this));//由于标签闭合,因此还原数据源
    }

    protected void fireEndEvent(EventType eventType) {
        eventSend.sendAll(eventType);
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
