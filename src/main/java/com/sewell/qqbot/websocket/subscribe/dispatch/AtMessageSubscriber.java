package com.sewell.qqbot.websocket.subscribe.dispatch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sewell.qqbot.api.ApiManager;
import com.sewell.qqbot.bot.BotService;
import com.sewell.qqbot.entity.Message;
import com.sewell.qqbot.exception.ApiException;
import com.sewell.qqbot.game.BotGame;
import com.sewell.qqbot.game.GameHelper;
import com.sewell.qqbot.game.constant.Commands;
import com.sewell.qqbot.game.constant.SolitaireGame;
import com.sewell.qqbot.util.SpringBeanUtils;
import com.sewell.qqbot.websocket.BotWebsocketClient;
import com.sewell.qqbot.websocket.entity.Payload;
import com.sewell.qqbot.websocket.event.AtMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sewell
 * @date 2022/10/6 8:47 PM
 */
@Slf4j
@Service
public class AtMessageSubscriber extends AbstractDispatchSubscriber implements ApplicationRunner {

    @Resource
    private ApiManager api;

    @Resource
    protected BotService botService;

    @Resource
    protected List<Commands> commands;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        commands.stream()
                .forEach(o->{
                    BotGame bean = (BotGame) SpringBeanUtils.getInstance().getBean(o.getBotGame());
                    o.getCmds().stream()
                            .forEach(s->{
                                GameHelper.cmdMatchGame.put(s, bean);
                            });
                });
    }

    @Override
    public void onSubscribe(Payload payload) {
        String e = payload.getT();
        BotWebsocketClient client = botService.getClient();
        if (!"AT_MESSAGE_CREATE".equals(e)) {
            return;
        }
        Message atMessage = JSON.toJavaObject((JSONObject) payload.getD(), Message.class);
        log.info(
                "[AtMessage]: 频道({}) 子频道({}) {}({}): {}",
                atMessage.getGuildId(),
                atMessage.getChannelId(),
                atMessage.getAuthor().getUsername(),
                atMessage.getAuthor().getId(),
                atMessage.getContent()
        );
//                if (client.getClientData().isRemoveAt()) {
        //去除@
        atMessage.setContent(atMessage.getContent().replaceAll("<@!" + client.getClientData().getRobotInfo().getId() + "> ", ""));
        atMessage.setContent(atMessage.getContent().replaceAll("<@!" + client.getClientData().getRobotInfo().getId() + ">", ""));
//                }
        AtMessageEvent atMessageEvent = new AtMessageEvent(this, atMessage);
        messageHandle(atMessageEvent);
    }

    private void messageHandle(AtMessageEvent atMessageEvent) {
        Message message = atMessageEvent.getMessage();
        message(message);
    }

    private void message(Message message) {
        String channelId = message.getChannelId();
        String content = message.getContent();
        String messageId = message.getId();
        try {
            String[] args = content.split(" ");
            String command = args[0];
            String gameUserKey = GameHelper.cacheUserGameKey(message);
            BotGame gameHandle = null;
            if (GameHelper.userPlayGame.containsKey(gameUserKey)) {
                gameHandle = GameHelper.userPlayGame.get(gameUserKey);
            }

            if (GameHelper.cmdMatchGame.containsKey(command)) {
                gameHandle = GameHelper.cmdMatchGame.get(command);
                GameHelper.userPlayGame.put(gameUserKey, gameHandle);
            }

            if (gameHandle == null) {
                log.error("无效的命令：{}", command);
                //兜底，使用成语接龙的handler
                gameHandle = GameHelper.cmdMatchGame.get(SolitaireGame.SolitaireGameEnum.SOLITAIRE_START.getCmd());
            }
            gameHandle.handle(message);

        } catch (ApiException e) {
            log.error("消息处理发生异常: {} {}({})", e.getCode(), e.getMessage(), e.getError());
//            api.getMessageApi().sendMessage(channelId, "消息处理失败: " + e.getMessage(), messageId);
            api.getMessageApi().sendMessageReference(channelId, "机器人维护中，请稍后再试", messageId);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
    }


}
