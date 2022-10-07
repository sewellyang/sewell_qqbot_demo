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

import com.sewell.qqbot.entity.PinsMessage;
import com.sewell.qqbot.properties.BotConfig;

/**
 * 精华消息相关接口
 *
 */
public class PinsMessageApi extends BaseApi {
    public PinsMessageApi(BotConfig botConfig) {
        super(botConfig);
    }

    /**
     * 添加精华消息
     *
     * @param channelId 子频道ID
     * @param messageId 消息ID
     * @return PinsMessage
     */
    public PinsMessage addPinsMessage(String channelId, String messageId) {
        return post("/channels/" + channelId + "/pins/" + messageId, null, PinsMessage.class);
    }

    /**
     * 删除精华消息
     *
     * @param channelId 子频道ID
     * @param messageId 消息ID
     */
    public void deletePinsMessage(String channelId, String messageId) {
        delete("/channels/" + channelId + "/pins/" + messageId, null);
    }

    /**
     * 获取精华消息
     *
     * @param channelId 子频道ID
     * @return PinsMessage
     */
    public PinsMessage getPinsMessage(String channelId) {
        return get("/channels/" + channelId + "/pins", PinsMessage.class);
    }

}
