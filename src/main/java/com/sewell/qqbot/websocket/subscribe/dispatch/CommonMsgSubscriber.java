package com.sewell.qqbot.websocket.subscribe.dispatch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sewell.qqbot.bot.BotService;
import com.sewell.qqbot.entity.Channel;
import com.sewell.qqbot.entity.Guild;
import com.sewell.qqbot.entity.Member;
import com.sewell.qqbot.entity.MessageAudited;
import com.sewell.qqbot.entity.MessageReaction;
import com.sewell.qqbot.websocket.BotWebsocketClient;
import com.sewell.qqbot.websocket.entity.Payload;
import com.sewell.qqbot.websocket.entity.Ready;
import com.sewell.qqbot.websocket.enums.OpEnum;
import com.sewell.qqbot.websocket.event.ChannelCreateEvent;
import com.sewell.qqbot.websocket.event.ChannelDeleteEvent;
import com.sewell.qqbot.websocket.event.ChannelUpdateEvent;
import com.sewell.qqbot.websocket.event.DirectMessageEvent;
import com.sewell.qqbot.websocket.event.GuildCreateEvent;
import com.sewell.qqbot.websocket.event.GuildDeleteEvent;
import com.sewell.qqbot.websocket.event.GuildMemberAddEvent;
import com.sewell.qqbot.websocket.event.GuildMemberRemoveEvent;
import com.sewell.qqbot.websocket.event.GuildMemberUpdateEvent;
import com.sewell.qqbot.websocket.event.GuildUpdateEvent;
import com.sewell.qqbot.websocket.event.MessageAuditPassEvent;
import com.sewell.qqbot.websocket.event.MessageAuditRejectEvent;
import com.sewell.qqbot.websocket.event.MessageReactionAddEvent;
import com.sewell.qqbot.websocket.event.MessageReactionRemoveEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author sewell
 * @date 2022/10/7 10:01 AM
 */
@Slf4j
@Service
public class CommonMsgSubscriber extends AbstractDispatchSubscriber {

    @Resource
    protected BotService botService;

    @Override
    public void onSubscribe(Payload payload) {
        String e = payload.getT();
        BotWebsocketClient client = botService.getClient();
        switch (e) {
            case "READY":
                Ready ready = JSON.toJavaObject((JSONObject) payload.getD(), Ready.class);
                client.getClientData().setSessionId(ready.getSessionId());
                client.getClientData().setRobotInfo(ready.getUser());
                log.info("鉴权成功, 机器人已上线!");
                break;
            case "GUILD_CREATE":
                Guild gc = JSON.toJavaObject((JSONObject) payload.getD(), Guild.class);
                log.info("新增频道: {}({})", gc.getName(), gc.getId());
                GuildCreateEvent guildCreateEvent = new GuildCreateEvent(this, gc);
                onGuildCreate(guildCreateEvent);
                break;
            case "GUILD_UPDATE":
                Guild gu = JSON.toJavaObject((JSONObject) payload.getD(), Guild.class);
                log.info("频道信息变更: {}({})", gu.getName(), gu.getId());
                GuildUpdateEvent guildUpdateEvent = new GuildUpdateEvent(this, gu);
                onGuildUpdate(guildUpdateEvent);
                break;
            case "GUILD_DELETE":
                Guild gd = JSON.toJavaObject((JSONObject) payload.getD(), Guild.class);
                log.info("退出频道: {}({})", gd.getName(), gd.getId());
                GuildDeleteEvent guildDeleteEvent = new GuildDeleteEvent(this, gd);
                onGuildDelete(guildDeleteEvent);
                break;
            case "CHANNEL_CREATE":
                Channel cc = JSON.toJavaObject((JSONObject) payload.getD(), Channel.class);
                log.info("子频道创建: {}({})", cc.getName(), cc.getId());
                ChannelCreateEvent channelCreateEvent = new ChannelCreateEvent(this, cc);
                onChannelCreate(channelCreateEvent);
                break;
            case "CHANNEL_UPDATE":
                Channel cu = JSON.toJavaObject((JSONObject) payload.getD(), Channel.class);
                log.info("子频道更新: {}({})", cu.getName(), cu.getId());
                ChannelUpdateEvent channelUpdateEvent = new ChannelUpdateEvent(this, cu);
                onChannelUpdate(channelUpdateEvent);
                break;
            case "CHANNEL_DELETE":
                Channel cd = JSON.toJavaObject((JSONObject) payload.getD(), Channel.class);
                log.info("子频道删除: {}({})", cd.getName(), cd.getId());
                ChannelDeleteEvent channelDeleteEvent = new ChannelDeleteEvent(this, cd);
                onChannelDelete(channelDeleteEvent);
                break;
            case "GUILD_MEMBER_ADD":
                Member ma = JSON.toJavaObject((JSONObject) payload.getD(), Member.class);
                log.info("频道用户增加: {}[{}]({})", ma.getUser().getUsername(), ma.getNick(), ma.getUser().getId());
                GuildMemberAddEvent guildMemberAddEvent = new GuildMemberAddEvent(this, ma);
                onGuildMemberAdd(guildMemberAddEvent);
                break;
            case "GUILD_MEMBER_UPDATE":
                Member mu = JSON.toJavaObject((JSONObject) payload.getD(), Member.class);
                log.info("频道用户更新: {}[{}]({})", mu.getUser().getUsername(), mu.getNick(), mu.getUser().getId());
                GuildMemberUpdateEvent guildMemberUpdateEvent = new GuildMemberUpdateEvent(this, mu);
                onGuildMemberUpdate(guildMemberUpdateEvent);
                break;
            case "GUILD_MEMBER_REMOVE":
                Member md = JSON.toJavaObject((JSONObject) payload.getD(), Member.class);
                log.info("频道用户删除: {}[{}]({})", md.getUser().getUsername(), md.getNick(), md.getUser().getId());
                GuildMemberRemoveEvent guildMemberRemoveEvent = new GuildMemberRemoveEvent(this, md);
                onGuildMemberRemove(guildMemberRemoveEvent);
                break;
            case "MESSAGE_REACTION_ADD":
                MessageReaction ra = JSON.toJavaObject((JSONObject) payload.getD(), MessageReaction.class);
                log.info("表情添加: {}({})", JSON.toJSONString(ra.getTarget()), ra.getChannelId());
                MessageReactionAddEvent messageReactionAddEvent = new MessageReactionAddEvent(this, ra);
                onMessageReactionAdd(messageReactionAddEvent);
                break;
            case "MESSAGE_REACTION_REMOVE":
                MessageReaction rr = JSON.toJavaObject((JSONObject) payload.getD(), MessageReaction.class);
                log.info("表情移除: {}({})", JSON.toJSONString(rr.getTarget()), rr.getChannelId());
                MessageReactionRemoveEvent messageReactionRemoveEvent = new MessageReactionRemoveEvent(this, rr);
                onMessageReactionRemove(messageReactionRemoveEvent);
                break;

            case "MESSAGE_AUDIT_PASS":
                MessageAudited ap = JSON.toJavaObject((JSONObject) payload.getD(), MessageAudited.class);
                log.info("消息审核通过: {}", ap);
                MessageAuditPassEvent messageAuditPassEvent = new MessageAuditPassEvent(this, ap);
                onMessageAuditPass(messageAuditPassEvent);
                break;
            case "MESSAGE_AUDIT_REJECT":
                MessageAudited ar = JSON.toJavaObject((JSONObject) payload.getD(), MessageAudited.class);
                log.info("消息审核不通过: {}", ar);
                MessageAuditRejectEvent messageAuditRejectEvent = new MessageAuditRejectEvent(this, ar);
                onMessageAuditReject(messageAuditRejectEvent);
                break;
            case "FORUM_THREAD_CREATE":
                com.sewell.qqbot.entity.forum.thread.Thread thread = JSON.toJavaObject((JSONObject) payload.getD(), com.sewell.qqbot.entity.forum.thread.Thread.class);
                log.info("[ForumThread]: {}: {}", thread.getAuthorId(), thread.getThreadInfo().getTitle());
                break;
            case "RESUMED":
                log.info("恢复连接成功, 离线消息已处理!");
                break;
            default:
                break;
        }
    }

    @Override
    public OpEnum eventType() {
        return OpEnum.DISPATCH;
    }



    private void onMessageAuditReject(MessageAuditRejectEvent messageAuditRejectEvent) {
    }

    private void onMessageAuditPass(MessageAuditPassEvent messageAuditPassEvent) {


    }

    private void onDirectMessage(DirectMessageEvent directMessageEvent) {


    }

    private void onMessageReactionRemove(MessageReactionRemoveEvent messageReactionRemoveEvent) {


    }

    private void onMessageReactionAdd(MessageReactionAddEvent messageReactionAddEvent) {
    }

    private void onGuildMemberRemove(GuildMemberRemoveEvent guildMemberRemoveEvent) {
    }

    private void onGuildMemberUpdate(GuildMemberUpdateEvent guildMemberUpdateEvent) {
    }

    private void onGuildMemberAdd(GuildMemberAddEvent guildMemberAddEvent) {
    }

    private void onChannelDelete(ChannelDeleteEvent channelDeleteEvent) {
    }

    private void onChannelUpdate(ChannelUpdateEvent channelUpdateEvent) {
    }

    private void onChannelCreate(ChannelCreateEvent channelCreateEvent) {
    }

    private void onGuildDelete(GuildDeleteEvent guildDeleteEvent) {

    }

    private void onGuildUpdate(GuildUpdateEvent guildUpdateEvent) {
    }

    private void onGuildCreate(GuildCreateEvent guildCreateEvent) {

    }
}
