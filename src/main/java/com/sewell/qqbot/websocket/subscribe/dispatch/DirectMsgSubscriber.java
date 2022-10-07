package com.sewell.qqbot.websocket.subscribe.dispatch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sewell.qqbot.api.ApiManager;
import com.sewell.qqbot.bot.BotService;
import com.sewell.qqbot.entity.Message;
import com.sewell.qqbot.websocket.BotWebsocketClient;
import com.sewell.qqbot.websocket.entity.Payload;
import com.sewell.qqbot.websocket.event.DirectMessageEvent;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author sewell
 * @date 2022/10/7 10:46 AM
 */
@Slf4j
@Service
public class DirectMsgSubscriber extends AbstractDispatchSubscriber{

    @Resource
    private ApiManager api;

    @Resource
    protected BotService botService;

    @Override
    public void onSubscribe(Payload payload) {
        String e = payload.getT();
        BotWebsocketClient client = botService.getClient();
        if (!"DIRECT_MESSAGE_CREATE".equals(e)) {
            return;
        }
        Message dm = JSON.toJavaObject((JSONObject) payload.getD(), Message.class);
        log.info("[DirectMessage]: {}({}): {}", dm.getAuthor().getUsername(), dm.getAuthor().getId(), dm.getContent());
        DirectMessageEvent directMessageEvent = new DirectMessageEvent(this, dm);
        onDirectMessage(directMessageEvent);

    }

    private void onDirectMessage(DirectMessageEvent event) {
        val message = event.getMessage();
        log.debug("{}", message);
        api.getDirectMessageApi().sendMessage(
                message.getGuildId(),
                "测试",
                message.getId()
        );
    }
}
