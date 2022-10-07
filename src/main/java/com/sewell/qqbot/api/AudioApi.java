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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sewell.qqbot.entity.AudioControl;
import com.sewell.qqbot.properties.BotConfig;

/**
 * 音频相关接口
 *
 */
public class AudioApi extends BaseApi {
    public AudioApi(BotConfig botConfig) {
        super(botConfig);
    }

    /**
     * 音频控制
     *
     * @param channelId 子频道ID
     * @param control   音频控制对象
     */
    public void audioControl(String channelId, AudioControl control) {
        post("/channels/" + channelId + "/audio", (JSONObject) JSON.toJSON(control), null);
    }

    /**
     * 机器人上麦
     *
     * @param channelId 子频道ID
     */
    public void openMic(String channelId) {
        put("/channels/" + channelId + "/mic", null, null);
    }

    /**
     * 机器人下麦
     *
     * @param channelId 子频道ID
     */
    public void closeMic(String channelId) {
        delete("/channels/" + channelId + "/mic", null);
    }
}
