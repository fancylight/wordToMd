package wToMd.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import wToMd.event.EventAccept;
import wToMd.event.EventSend;
import wToMd.event.EventType;

import java.util.List;

import static wToMd.table.TableXmlDefine.TABLE_TAG;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-11 16:24
 **/
public abstract class CustomHandler<T> extends DefaultHandler implements DataAccept<T> {
    private StringBuilder result = new StringBuilder();
    private EventSend eventSend;
    protected List<EventAccept> list;

    public abstract T getResult();

    /**
     * 子类调用
     */
    public void initParse() {
        list.forEach(eventAccept -> eventAccept.addDataAccept(this));
        eventSend = new EventSend(list);
    }
    //---------------------一些open xml定义的标签-----------------------

    private String currentTag;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentTag = qName;
        if (qName.equals(TABLE_TAG))
            eventSend.sendAll(EventType.TABLEBEGIN);
        eventSend.dealStartEle(uri, localName, qName, attributes, result);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        eventSend.dealEndEle(uri, localName, qName, result);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String text = new String(ch, start, length);
        if (text.contains("\n") || text.contains("\r"))
            return;
        eventSend.sendData(currentTag, text);
    }
}
