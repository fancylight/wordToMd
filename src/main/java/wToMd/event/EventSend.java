package wToMd.event;

import org.xml.sax.Attributes;

import java.util.List;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-11 16:45
 **/
public class EventSend {
    private List<EventAccept> list;

    public EventSend(List<EventAccept> list) {
        this.list = list;
    }

    public void sendAll(EventType eventType) {
        list.forEach(eventAccept -> eventAccept.acceptEvent(eventType));
    }

    public void dealStartEle(String uri, String localName, String qName, Attributes attributes, StringBuilder result) {
        list.forEach(eventAccept -> eventAccept.dealStartEle(uri, localName, qName, attributes));
    }

    public void sendData(String tag, String context) {
        list.forEach(eventAccept -> eventAccept.dealText(tag, context));
    }

    public void dealEndEle(String uri, String localName, String qName, StringBuilder result) {
        list.forEach(eventAccept -> eventAccept.endEle(uri,localName,qName));
    }
}
