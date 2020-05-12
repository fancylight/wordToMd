package wToMd.table;

import wToMd.event.EventAccept;
import wToMd.event.EventType;
import wToMd.sax.DataAccept;

public abstract class Parse<T extends ContextBuild> implements EventAccept {
    protected EventType currentEvent;
    protected T contextBuild;
    protected EventType[] acceptEvent;
    protected Class<T> contextBuildClass;
    protected DataAccept dataAccept;

    public Parse() {
    }

    public Parse(Class<T> contextBuildClass) {
        this.contextBuildClass = contextBuildClass;
        initEvent();
    }

    public void acceptEvent(EventType eventType) {
        this.currentEvent = eventType;
    }

    protected void initEvent() {
        //当dataAccept为空,表示该解析器接受所有事件
    }


    @Override
    public boolean support() {
        if (dataAccept == null)
            return true;
        for (EventType eventType : acceptEvent) {
            if (eventType == currentEvent)
                return true;
        }
        return false;
    }

    public abstract Object buildTableResult();

    protected ContextBuild newBuild() {
        if (contextBuildClass != null) {
            try {
                return contextBuildClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected  void clear(){

    }
    //---------获取数据---------------

    @Override
    public void addDataAccept(DataAccept dataAccept) {
        this.dataAccept = dataAccept;
    }

    @Override
    public void acceptData() {
        Object o = buildTableResult();
        this.dataAccept.acceptData(o);
    }
}
