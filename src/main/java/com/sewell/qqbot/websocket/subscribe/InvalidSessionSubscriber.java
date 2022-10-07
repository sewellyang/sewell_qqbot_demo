package com.sewell.qqbot.websocket.subscribe;

import com.sewell.qqbot.websocket.entity.Payload;
import com.sewell.qqbot.websocket.enums.OpEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author sewell
 * @date 2022/10/6 7:15 PM
 */
@Slf4j
@Service
public class InvalidSessionSubscriber implements DataSubscriber{
    @Override
    public void onSubscribe(Payload payload) {
        log.error("鉴权失败!");
    }

    @Override
    public OpEnum eventType() {
        return OpEnum.INVALID_SESSION;
    }
}
