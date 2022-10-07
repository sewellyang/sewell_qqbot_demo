package com.sewell.qqbot.websocket.subscribe;

import com.sewell.qqbot.websocket.entity.Payload;
import com.sewell.qqbot.websocket.enums.OpEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author sewell
 * @date 2022/10/6 7:14 PM
 */
@Slf4j
@Service
public class ReconnectSubscriber implements DataSubscriber{

    @Override
    public void onSubscribe(Payload payload) {
        log.info("服务端通知重连");
    }

    @Override
    public OpEnum eventType() {
        return OpEnum.RECONNECT;
    }
}
