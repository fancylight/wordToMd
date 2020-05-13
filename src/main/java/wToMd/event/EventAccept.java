package wToMd.event;

import org.xml.sax.Attributes;
import wToMd.common.EventSource;
import wToMd.sax.DataAccept;

/**
 * 事件监听器接口
 */
public interface EventAccept extends DataAccept {

    //------事件处理--------------
    EventSource getEventSource();

    void addEventSource(EventSource eventSource);

    void acceptEvent(EventType eventType);

    boolean support(boolean isBegin);

    boolean support(EventType eventType);

    //-------------处理三种情况元素-----------------
    void dealStartEle(String uri, String localName, String qName, Attributes attributes);

    void endEle(String uri, String localName, String qName);

    void dealText(String tag, String context);

    //-------------数据发送--------------------
    void changeDataAccept(DataAccept dataAccept);

    void sendData();

}
