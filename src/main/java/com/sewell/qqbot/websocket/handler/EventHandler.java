package com.sewell.qqbot.websocket.handler;

import com.sewell.qqbot.websocket.entity.Payload;
import com.sewell.qqbot.websocket.subscribe.DataSubscriber;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author sewell
 * @date 2022/10/6 7:25 PM
 */
@Slf4j
public abstract class EventHandler implements DataHandler{

    protected List<DataSubscriber> dataSubscribers;

    public EventHandler(List<DataSubscriber> subscriberList) {
        this.dataSubscribers = subscriberList;
    }

    @Override
    public void handle(Payload payload) {
        dataSubscribers.stream().forEach(subscriber -> {
            try {
                subscriber.onSubscribe(payload);
            } catch (Exception e) {
                log.error("subscriber出现异常：payload:{}", payload, e);
            }
        });
    }

}
