package wToMd.event;

import org.xml.sax.Attributes;
import wToMd.common.EventSource;

import java.util.List;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-11 16:45
 **/
public class EventSend {
    private List<EventAccept> list;

    public EventSend(List<EventAccept> list, EventSource eventSource) {
        this.list = list;
        list.forEach(eventAccept -> eventAccept.addEventSource(eventSource));
    }

    public void sendAll(EventType eventType) {
        list.forEach(eventAccept -> eventAccept.acceptEvent(eventType));
    }

    public void dealStartEle(String uri, String localName, String qName, Attributes attributes) {
        list.forEach(eventAccept -> eventAccept.dealStartEle(uri, localName, qName, attributes));
    }

    public void sendData(String tag, String context) {
        list.forEach(eventAccept -> eventAccept.dealText(tag, context));
    }

    public void dealEndEle(String uri, String localName, String qName) {
        list.forEach(eventAccept -> eventAccept.endEle(uri,localName,qName));
    }
}
