
package com.sewell.qqbot.api;

import com.sewell.qqbot.properties.BotConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * API 管理器
 *
 */
@Component
public class ApiManager {
    /**
     * 访问信息
     */
    @Resource
    private BotConfig botConfig;

    /**
     * 获取 消息API 实例
     */
    public MessageApi getMessageApi() {
        return new MessageApi(botConfig);
    }

    /**
     * 获取 表情表态API 实例
     */
    public MessageReactionApi getMessageReactionApi() {
        return new MessageReactionApi(botConfig);
    }

    /**
     * 获取 频道API 实例
     */
    public GuildApi getGuildApi() {
        return new GuildApi(botConfig);
    }

    /**
     * 获取 子频道API 实例
     */
    public ChannelApi getChannelApi() {
        return new ChannelApi(botConfig);
    }

    /**
     * 获取 子频道权限API 实例
     */
    public ChannelPermissionsApi getChannelPermissionsApi() {
        return new ChannelPermissionsApi(botConfig);
    }

    /**
     * 获取 日程API 实例
     */
    public ScheduleApi getScheduleApi() {
        return new ScheduleApi(botConfig);
    }

    /**
     * 获取 音频API 实例
     */
    public AudioApi getAudioApi() {
        return new AudioApi(botConfig);
    }

    /**
     * 获取 身份组API 实例
     */
    public RoleApi getRoleApi() {
        return new RoleApi(botConfig);
    }

    /**
     * 获取 成员API 实例
     */
    public MemberApi getMemberApi() {
        return new MemberApi(botConfig);
    }

    /**
     * 获取 公告API 实例
     */
    public AnnouncesApi getAnnouncesApi() {
        return new AnnouncesApi(botConfig);
    }

    /**
     * 获取 精华消息API 实例
     */
    public PinsMessageApi getPinsMessageApi() {
        return new PinsMessageApi(botConfig);
    }

    /**
     * 获取 禁言API 实例
     */
    public MuteApi getMuteApi() {
        return new MuteApi(botConfig);
    }

    /**
     * 获取 用户API 实例
     */
    public UserApi getUserApi() {
        return new UserApi(botConfig);
    }

    /**
     * 获取 私信API 实例
     */
    public DirectMessageApi getDirectMessageApi() {
        return new DirectMessageApi(botConfig);
    }

    /**
     * 获取 帖子API 实例
     */
    public ForumApi getForumApi() {
        return new ForumApi(botConfig);
    }
}
