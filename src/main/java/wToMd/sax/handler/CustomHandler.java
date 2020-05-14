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
import java.util.Stack;
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
    protected Stack<EventType> eventTypeStack = new Stack(); //栈顶表示当前执行事件

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentTag = qName;
        EventType eventType = null;
        if (qName.equals(TABLE_TAG)) {//表单
            eventType = EventType.TABLEBEGIN;
        } else if (qName.equals(PIC_BLIPFILL_TAG)) {//图片
            eventType = EventType.PICBEGIN;
        }
        fireBeginEvent(eventType);
        eventSend.dealStartEle(uri, localName, qName, attributes);
    }

    private void fireBeginEvent(EventType eventType) {
        if (eventTypeStack.size() == 0) {
            if (eventType != null) {
                log.info("新的事件" + eventType);
                eventTypeStack.push(eventType);
            }
            eventSend.sendAll(eventType);
            return;
        }
        EventType eventTypeNow = eventTypeStack.peek();
        if (eventType == null) {//保持事件
            eventType = eventTypeNow;
        } else {
            log.info(String.format("发生%s内嵌%s", eventTypeNow, eventType));
            eventTypeStack.push(eventType);
            changeAccept(eventType, eventTypeNow);
        }
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
        EventType eventType = null;
        if (qName.equals(PIC_BLIPFILL_TAG)) { //图片处理结束
            eventType = EventType.PICEND;
        } else if (qName.equals(TABLE_TAG)) {//表格处理结束
            eventType = EventType.TABLEEND;
        }
        fireEndEvent(eventType);
        eventSend.dealEndEle(uri, localName, qName);

    }

    protected void fireEndEvent(EventType eventType) {
        if (eventTypeStack.size() == 0) {
            if (eventType == null) {
                eventSend.sendAll(null);
            } else {
                log.warn("未出现对应的开始事件" + eventType, new RuntimeException());
            }
            return;
        }
        if (eventType == null) {
            eventType = eventTypeStack.peek();
        } else {
            EventType eventTypePre = eventTypeStack.pop();
            log.info(String.format("结束事件%s,弹出对应开始事件%s", eventType, eventTypePre));
            list.forEach(eventAccept -> eventAccept.changeDataAccept(this));//由于标签闭合,因此还原数据源
        }
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
