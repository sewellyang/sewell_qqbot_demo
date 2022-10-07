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

import com.sewell.qqbot.entity.MessageReaction;
import lombok.Getter;

import java.util.EventObject;

/**
 * 表态添加事件
 *
 * @author 真心
 * @since 2021/12/19 16:03
 */
@Getter
public class MessageReactionAddEvent extends EventObject {
    private final MessageReaction reaction;

    public MessageReactionAddEvent(Object source, MessageReaction reaction) {
        super(source);
        this.reaction = reaction;
    }
}
