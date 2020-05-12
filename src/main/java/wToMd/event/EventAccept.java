package wToMd.event;

import org.xml.sax.Attributes;
import wToMd.sax.DataAccept;

public interface EventAccept {
    void acceptEvent(EventType eventType);

    boolean support();

    void dealStartEle(String uri, String localName, String qName, Attributes attributes);

    void endEle(String uri, String localName, String qName);

    void dealText(String tag, String context);

    void addDataAccept(DataAccept dataAccept);

    void acceptData();
}
