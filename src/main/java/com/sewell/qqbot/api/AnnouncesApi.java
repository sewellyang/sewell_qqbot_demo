/*
 * qq-official-bot-sdk - QQ Official Bot SDK For Java
 * Copyright (C) 2021-2022 xiaoye-bot Project Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sewell.qqbot.api;

import com.sewell.qqbot.entity.Announces;
import com.sewell.qqbot.entity.RecommendChannel;
import com.sewell.qqbot.exception.ApiException;
import com.sewell.qqbot.properties.BotConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 公告相关接口
 *
 */
public class AnnouncesApi extends BaseApi {
    public AnnouncesApi(BotConfig botConfig) {
        super(botConfig);
    }


    /**
     * 创建频道全局公告
     *
     * @param guildId           频道ID
     * @param channelId         子频道ID
     * @param messageId         消息ID
     * @param recommendChannels 推荐子频道列表 (可选)
     * @return {@link Announces} 对象
     */
    public Announces createGuildAnnounces(String guildId, String channelId, String messageId, RecommendChannel[] recommendChannels) throws ApiException {
        Map<String, Object> data = new HashMap<>();
        data.put("message_id", messageId);
        data.put("channel_id", channelId);
        if (recommendChannels != null) {
            data.put("recommend_channels", recommendChannels);
        }
        return post("/guilds/" + guildId + "/announces", data, Announces.class);
    }

    /**
     * 删除频道全局公告
     *
     * @param guildId   频道ID
     * @param messageId 消息ID
     */
    public void deleteGuildAnnounces(String guildId, String messageId) throws ApiException {
        delete("/guilds/" + guildId + "/announces/" + messageId, null);
    }

    /**
     * 创建子频道公告
     *
     * @param channelId 子频道ID
     * @param messageId 消息ID
     * @deprecated 已废弃，请使用 {@link PinsMessageApi#addPinsMessage(String, String)}
     */
    @Deprecated
    public Announces createAnnounces(String channelId, String messageId) throws ApiException {
        Map<String, Object> data = new HashMap<>();
        data.put("message_id", messageId);
        return post("/channels/" + channelId + "/announces", data, Announces.class);
    }

    /**
     * 删除子频道公告
     *
     * @param channelId 子频道ID
     * @param messageId 消息ID
     * @deprecated 已废弃，请使用 {@link PinsMessageApi#deletePinsMessage(String, String)}
     */
    @Deprecated
    public void deleteAnnounces(String channelId, String messageId) throws ApiException {
        delete("/channels/" + channelId + "/announces/" + messageId, null);
    }
}
