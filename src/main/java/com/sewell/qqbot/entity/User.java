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
 * 用户对象
 *
 * @author 真心
 * @since 2021/12/8 16:26
 */
@Data
public class User {
    /**
     * 用户ID
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户头像地址
     */
    private String avatar;
    /**
     * 是否是机器人
     */
    private Boolean bot;
    /**
     * 特殊关联应用的 openid
     */
    private String unionOpenId;
    /**
     * 机器人关联的互联应用的用户信息
     */
    private String unionUserAccount;
}
