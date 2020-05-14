package wToMd.sax;

import wToMd.common.AbstractParse;

public interface DataAccept<T> {
    void acceptData(T data, AbstractParse abstractParse);
}
