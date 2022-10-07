package com.sewell.qqbot.websocket;

import com.sewell.qqbot.websocket.entity.Payload;
import com.sewell.qqbot.websocket.enums.OpEnum;
import com.sewell.qqbot.websocket.handler.DataHandler;
import com.sewell.qqbot.websocket.handler.DispatchEventHandler;
import com.sewell.qqbot.websocket.handler.HeartBeatEventHandler;
import com.sewell.qqbot.websocket.handler.HelloEventHandler;
import com.sewell.qqbot.websocket.handler.InvalidSessionEventHandler;
import com.sewell.qqbot.websocket.handler.ReconnectEventHandler;
import com.sewell.qqbot.websocket.subscribe.DataSubscriber;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Websocket cache handler.
 *
 * @author xiaoyu(Myth)
 */
public class WebsocketDataHandler {

    private static final EnumMap<OpEnum, DataHandler> ENUM_MAP = new EnumMap<>(OpEnum.class);

    public WebsocketDataHandler(final List<DataSubscriber> dataSubscriberList) {
        Map<OpEnum, List<DataSubscriber>> dataSubs = dataSubscriberList.stream()
                .collect(Collectors.groupingBy(DataSubscriber::eventType));
        ENUM_MAP.put(OpEnum.DISPATCH, new DispatchEventHandler(dataSubs.get(OpEnum.DISPATCH)));
        ENUM_MAP.put(OpEnum.RECONNECT, new ReconnectEventHandler(dataSubs.get(OpEnum.RECONNECT)));
        ENUM_MAP.put(OpEnum.INVALID_SESSION, new InvalidSessionEventHandler(dataSubs.get(OpEnum.INVALID_SESSION)));
        ENUM_MAP.put(OpEnum.HELLO, new HelloEventHandler(dataSubs.get(OpEnum.HELLO)));
        ENUM_MAP.put(OpEnum.HEART_BEAT, new HeartBeatEventHandler(dataSubs.get(OpEnum.HEART_BEAT)));
    }

    public void executor(final OpEnum type, final Payload payload) {
        ENUM_MAP.get(type).handle(payload);
    }
}
