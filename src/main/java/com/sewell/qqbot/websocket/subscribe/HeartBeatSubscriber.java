package com.sewell.qqbot.websocket.subscribe;

import com.sewell.qqbot.websocket.entity.Payload;
import com.sewell.qqbot.websocket.enums.OpEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author sewell
 * @date 2022/10/6 7:16 PM
 */
@Slf4j
@Service
public class HeartBeatSubscriber implements DataSubscriber{
    @Override
    public void onSubscribe(Payload payload) {
        log.debug("收到服务器心跳");
    }

    @Override
    public OpEnum eventType() {
        return OpEnum.HEART_BEAT;
    }


}
