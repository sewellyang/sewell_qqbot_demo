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

package com.sewell.qqbot.websocket.event;

import com.sewell.qqbot.entity.Channel;
import lombok.Getter;

import java.util.EventObject;

/**
 * 子频道更新事件
 *
 * @author 真心
 * @since 2021/12/19 11:59
 */
@Getter
public class ChannelUpdateEvent extends EventObject {
    private final Channel channel;

    public ChannelUpdateEvent(Object source, Channel channel) {
        super(source);
        this.channel = channel;
    }
}