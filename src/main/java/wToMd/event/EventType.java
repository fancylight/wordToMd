package wToMd.event;

public enum EventType {
    TABLEBEGIN(true), TABLEEND(false), TRBEGIN(true),
    PICBEGIN(true), PICEND(false);

    EventType(boolean isBegin) {
    }
}
