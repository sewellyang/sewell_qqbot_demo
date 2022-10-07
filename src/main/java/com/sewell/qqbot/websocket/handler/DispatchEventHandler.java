package com.sewell.qqbot.websocket.handler;

import com.sewell.qqbot.websocket.entity.Payload;
import com.sewell.qqbot.websocket.subscribe.DataSubscriber;

import java.util.List;

/**
 * @author sewell
 * @date 2022/10/6 7:10 PM
 */
public class DispatchEventHandler extends EventHandler {

    public DispatchEventHandler(List<DataSubscriber> subscriberList) {
        super(subscriberList);
    }

    @Override
    public void handle(Payload payload) {
        super.handle(payload);
    }
}
