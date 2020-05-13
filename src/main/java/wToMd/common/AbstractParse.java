package wToMd.common;

import wToMd.event.EventAccept;
import wToMd.event.EventType;
import wToMd.sax.DataAccept;

public abstract class AbstractParse<T extends ContextBuild> implements EventAccept {
    protected EventType currentEvent;
    protected T contextBuild;
    protected EventType[] acceptEvent;
    protected Class<T> contextBuildClass;
    protected DataAccept dataAccept;

    //--------------------构造器------------------------------
    public AbstractParse() {
        initEvent();
    }

    public AbstractParse(boolean isInner) {
        this();
    }

    public AbstractParse(Class<T> contextBuildClass, boolean isInner) {
        this(isInner);
        this.contextBuildClass = contextBuildClass;
    }

    //-----------------------事件处理-------------------------------------------
    public void acceptEvent(EventType eventType) {
        this.currentEvent = eventType;
    }

    protected void initEvent() {
        //当dataAccept为空,表示该解析器接受所有事件
    }


    @Override
    public boolean support() {
        if (acceptEvent == null) //表示接受所有类型
            return true;
        for (EventType eventType : acceptEvent) {
            if (eventType == currentEvent)
                return true;
        }
        return false;
    }

    @Override
    public boolean support(EventType eventType) {
        if (eventType == null) //null表示支持所有事件
            return true;
        for (EventType accept : acceptEvent) {
            if (accept == eventType)
                return true;
        }
        return false;
    }

    protected EventSource eventSource;

    @Override
    public EventSource getEventSource() {
        return eventSource;
    }

    @Override
    public void addEventSource(EventSource eventSource) {
        this.eventSource = eventSource;
    }
    //---------------------构建过程-------------------------

    /**
     * 构建数据,调用{@link ContextBuild}来创建此标签数据
     *
     * @return
     */
    public abstract Object buildTableResult();

    protected T newBuild() {
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

    protected void clear() {

    }
    //---------发送数据---------------

    @Override
    public void changeDataAccept(DataAccept dataAccept) {
        this.dataAccept = dataAccept;
    }

    /**
     * 该函数由子类调用,一般将产生的数据存放在此处
     */
    @Override
    public void sendData() {
        Object o = buildTableResult();
        this.dataAccept.acceptData(o);
    }
}
