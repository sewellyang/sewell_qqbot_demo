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
 * 音频控制对象
 *
 */
@Data
public class AudioControl {
    /**
     * 音频数据的url 可选, status为0时传入
     */
    private String audioUrl;
    /**
     * 状态文本 可选, status为0时传入
     */
    private String text;
    /**
     * 播放状态
     * <ul>
     *     <li>0: 开始播放操作</li>
     *     <li>1: 暂停播放操作</li>
     *     <li>2: 继续播放操作</li>
     *     <li>3: 停止播放操作</li>
     * </ul>
     */
    private Integer status;
}
