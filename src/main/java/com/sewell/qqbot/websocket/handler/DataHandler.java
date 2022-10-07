package com.sewell.qqbot.websocket.handler;

import com.sewell.qqbot.websocket.entity.Payload;

/**
 * The interface Data handler.
 *
 */
public interface DataHandler {

    /**
     * Handle.
     *
     * @param json  the data for json
     * @param eventType the event type
     */
    void handle(Payload payload);
}
