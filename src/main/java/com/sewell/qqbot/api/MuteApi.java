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

import com.sewell.qqbot.properties.BotConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 禁言相关接口
 *
 */
public class MuteApi extends BaseApi {
    public MuteApi(BotConfig botConfig) {
        super(botConfig);
    }

    /**
     * 全员禁言
     *
     * @param guildId  频道ID
     * @param duration 禁言时长，单位秒，0为解除禁言
     */
    public void muteAll(String guildId, Integer duration) {
        Map<String, Object> data = new HashMap<>();
        data.put("mute_seconds", duration.toString());
        patch("/guilds/" + guildId + "/mute", data, null);
    }

    /**
     * 禁言
     *
     * @param guildId  频道ID
     * @param userId   用户ID
     * @param duration 禁言时长，单位秒，0为解除禁言
     */
    public void mute(String guildId, String userId, Integer duration) {
        Map<String, Object> data = new HashMap<>();
        data.put("mute_seconds", duration.toString());
        patch("/guilds/" + guildId + "/members/" + userId + "/mute", data, null);
    }

    /**
     * 批量禁言
     *
     * @param guildId  频道ID
     * @param userIds  用户ID列表
     * @param duration 禁言时长，单位秒，0为解除禁言
     */
    public void muteBatch(String guildId, String[] userIds, Integer duration) {
        Map<String, Object> data = new HashMap<>();
        data.put("mute_seconds", duration.toString());
        data.put("user_ids", userIds);
        patch("/guilds/" + guildId + "/mute", data, null);
    }
}
