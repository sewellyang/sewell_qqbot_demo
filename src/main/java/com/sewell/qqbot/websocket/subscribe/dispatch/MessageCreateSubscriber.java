package com.sewell.qqbot.websocket.subscribe.dispatch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sewell.qqbot.api.ApiManager;
import com.sewell.qqbot.bot.BotService;
import com.sewell.qqbot.entity.Message;
import com.sewell.qqbot.websocket.BotWebsocketClient;
import com.sewell.qqbot.websocket.entity.Payload;
import com.sewell.qqbot.websocket.event.UserMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author sewell
 * @date 2022/10/7 10:09 AM
 */
@Service
@Slf4j
public class MessageCreateSubscriber extends AbstractDispatchSubscriber {
    @Resource
    private BotService botService;

    @Resource
    private ApiManager api;

    @Override
    public void onSubscribe(Payload payload) {
        String e = payload.getT();
        BotWebsocketClient client = botService.getClient();
        if (!"MESSAGE_CREATE".equals(e)) {
            return;
        }
        Message userMessage = JSON.toJavaObject((JSONObject) payload.getD(), Message.class);
        log.info(
                "[UserMessage]: 频道({}) 子频道({}) {}({}): {}",
                userMessage.getGuildId(),
                userMessage.getChannelId(),
                userMessage.getAuthor().getUsername(),
                userMessage.getAuthor().getId(),
                userMessage.getContent()
        );
        UserMessageEvent userMessageEvent = new UserMessageEvent(this, userMessage);
//        messageHandle(userMessageEvent);
    }


}
