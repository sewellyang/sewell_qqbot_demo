package com.sewell.qqbot.websocket.subscribe;

import com.sewell.qqbot.bot.BotService;
import com.sewell.qqbot.websocket.entity.Payload;
import com.sewell.qqbot.websocket.enums.OpEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author sewell
 * @date 2022/10/6 7:15 PM
 */
@Slf4j
@Service
public class HelloSubscriber implements DataSubscriber{

    @Resource
    private BotService botService;


    @Override
    public void onSubscribe(Payload payload) {
        botService.helloHandle(payload);
    }

    @Override
    public OpEnum eventType() {
        return OpEnum.HELLO;
    }
}
