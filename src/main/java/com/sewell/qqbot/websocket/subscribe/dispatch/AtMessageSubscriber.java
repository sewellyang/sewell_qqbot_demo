package com.sewell.qqbot.websocket.subscribe.dispatch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sewell.qqbot.api.ApiManager;
import com.sewell.qqbot.bot.BotService;
import com.sewell.qqbot.entity.DirectMessageSession;
import com.sewell.qqbot.entity.Message;
import com.sewell.qqbot.entity.MessageMarkdown;
import com.sewell.qqbot.entity.api.ApiPermission;
import com.sewell.qqbot.entity.api.ApiPermissionDemand;
import com.sewell.qqbot.entity.api.ApiPermissionDemandIdentify;
import com.sewell.qqbot.exception.ApiException;
import com.sewell.qqbot.template.EmbedTemplate;
import com.sewell.qqbot.template.TextThumbnailTemplate;
import com.sewell.qqbot.websocket.BotWebsocketClient;
import com.sewell.qqbot.websocket.entity.Payload;
import com.sewell.qqbot.websocket.event.AtMessageEvent;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sewell
 * @date 2022/10/6 8:47 PM
 */
@Slf4j
@Service
public class AtMessageSubscriber extends AbstractDispatchSubscriber {

    @Resource
    private ApiManager api;

    @Resource
    protected BotService botService;


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
        val message = atMessageEvent.getMessage();
        message(message);
    }

    private void message(Message message) {
        val guildId = message.getGuildId();
        val channelId = message.getChannelId();
        val content = message.getContent();
        val messageId = message.getId();
        val author = message.getAuthor();
        try {
            val args = content.split(" ");
            val command = args[0];
            switch (command) {
                case "threads":
                    List<Thread> threads = api.getForumApi().getThreadList(channelId);
                    log.info("{}", threads);
                    api.getMessageApi().sendMessage(channelId, Arrays.toString(threads.toArray()), messageId);
                    break;
                case "md":
                    MessageMarkdown md = new MessageMarkdown();
                    md.setContent("测试");
                    api.getMessageApi().sendMessage(channelId, md);
                    break;
                case "error":
                    api.getMessageApi().sendMessage(
                            channelId,
                            "https://www.qq.com",
                            messageId
                    );
                    break;
                case "apiList":
                    List<ApiPermission> permissions = api.getGuildApi().getApiPermissions(guildId);
                    for (ApiPermission permission : permissions) {
                        log.debug("{}", permission);
                        api.getMessageApi().sendMessage(
                                channelId,
                                permission.toString(),
                                messageId
                        );
                    }
                    break;
                case "apiLink":
                    ApiPermissionDemandIdentify identify = new ApiPermissionDemandIdentify();
                    identify.setPath("/guilds/{guild_id}/members/{user_id}");
                    identify.setMethod("GET");
                    ApiPermissionDemand demand = api.getGuildApi().createApiPermissionDemand(
                            guildId,
                            channelId,
                            identify,
                            "测试"
                    );
                    api.getMessageApi().sendMessage(
                            channelId,
                            demand.toString(),
                            messageId);
                    break;
                case "dm":
                    DirectMessageSession dms = api.getDirectMessageApi().createSession(
                            author.getId(),
                            guildId
                    );
                    api.getDirectMessageApi().sendMessage(
                            dms.getGuildId(),
                            "主动私信测试",
                            null
                    );
                    break;
                case "push":
                    api.getMessageApi().sendMessage(
                            channelId,
                            "测试",
                            null
                    );
                    break;
                case "members":
                    val members = api.getGuildApi().getGuildMembers(guildId, 1000);
                    for (val member : members) {
                        member.getUser().setAvatar("");
                    }
                    api.getMessageApi().sendMessage(
                            channelId,
                            members.toString(),
                            messageId
                    );
                    break;
                case "info":
                    api.getChannelPermissionsApi().addChannelPermissions(channelId, author.getId(), 1);
                    break;
                case "muteAll":
                    api.getMuteApi().muteAll(guildId, 300);
                    break;
                case "muteMe":
                    api.getMuteApi().mute(guildId, author.getId(), 300);
                    java.lang.Thread.sleep(6000);
                    api.getMuteApi().mute(guildId, author.getId(), 0);
                    break;
                case "dMsg":
                    val m = api.getMessageApi().sendMessage(channelId, "你好", messageId);
                    val id = m.getId();
                    api.getMessageApi().deleteMessage(channelId, id);
                    break;
                case "embed":
                    val fields = new ArrayList<String>();
                    fields.add("测试");
                    fields.add("测试2");
                    fields.add(String.valueOf(System.currentTimeMillis()));
                    val embed = EmbedTemplate.builder()
                            .title("测试")
                            .prompt("[测试]Embed")
                            .thumbnail("https://b.armoe.cn/assets/image/logo.png")
                            .fields(fields)
                            .build().toMessageEmbed();
                    api.getMessageApi().sendMessage(channelId, embed, messageId);
                    break;
                case "ping":
                    api.getMessageApi().sendMessage(channelId, "pong", messageId);
                    break;
                case "ark":
                    val ark = TextThumbnailTemplate.builder()
                            .build().toMessageArk();
                    api.getMessageApi().sendMessage(channelId, ark, messageId);
                    break;
            }
        } catch (ApiException e) {
            log.error("消息处理发生异常: {} {}({})", e.getCode(), e.getMessage(), e.getError());
            api.getMessageApi().sendMessage(channelId, "消息处理失败: " + e.getMessage(), messageId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
