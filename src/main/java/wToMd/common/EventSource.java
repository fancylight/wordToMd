package wToMd.common;

import wToMd.event.EventAccept;

import java.util.List;

public interface EventSource {
    List<EventAccept> getAllEventAccept();
}
