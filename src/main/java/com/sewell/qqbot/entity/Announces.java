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

package com.sewell.qqbot.entity;

import lombok.Data;

/**
 * 公告对象
 *
 */
@Data
public class Announces {
    /**
     * 频道ID
     */
    private String guildId;
    /**
     * 子频道ID
     */
    private String channelId;
    /**
     * 消息ID
     */
    private String messageId;
    /**
     * 公告类型
     * <ul>
     *     <li>0: 成员公告</li>
     *     <li>1: 欢迎公告</li>
     * </ul>
     */
    private int announcesType;
    /**
     * 推荐子频道列表
     */
    private RecommendChannel[] recommendChannels;
}
