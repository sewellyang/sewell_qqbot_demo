package com.sewell.qqbot.websocket.subscribe;

import com.sewell.qqbot.websocket.entity.Payload;
import com.sewell.qqbot.websocket.enums.OpEnum;

/**
 * @author sewell
 * @date 2022/10/6 7:06 PM
 */
public interface DataSubscriber {

    void onSubscribe(Payload payload);

    OpEnum eventType();
}
