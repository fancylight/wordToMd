package wToMd.event;

public enum EventType {
    TABLEBEGIN(true), TABLEEND(false), TRBEGIN(true), TABLETCBEGIN(true), TABLETCEND(true),

    PICBEGIN(true), PICEND(false),

    PBEGIN(true), PENG(false);

    private boolean isBegin;

    EventType(boolean isBegin) {
        this.isBegin = isBegin;
    }

    public boolean isBegin() {
        return isBegin;
    }

    public void setBegin(boolean begin) {
        isBegin = begin;
    }
}
