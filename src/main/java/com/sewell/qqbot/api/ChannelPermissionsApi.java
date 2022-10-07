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

import com.sewell.qqbot.entity.ChannelPermissions;
import com.sewell.qqbot.properties.BotConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 子频道权限相关接口
 *
 */
public class ChannelPermissionsApi extends BaseApi {
    public ChannelPermissionsApi(BotConfig botConfig) {
        super(botConfig);
    }

    /**
     * 获取子频道权限
     *
     * @param channelId 子频道ID
     * @param userId    用户ID
     * @return 子频道权限对象
     */
    public ChannelPermissions getChannelPermissions(String channelId, String userId) {
        return get("/channels/" + channelId + "/members/" + userId + "/permissions", ChannelPermissions.class);
    }

    /**
     * 添加子频道权限
     *
     * @param channelId   子频道ID
     * @param userId      用户ID
     * @param permissions 权限 <a href="https://bot.q.qq.com/wiki/develop/api/openapi/channel_permissions/model.html#permissions">权限列表</a>
     */
    public void addChannelPermissions(String channelId, String userId, Integer permissions) {
        Map<String, Object> data = new HashMap<>();
        data.put("add", permissions.toString());
        put("/channels/" + channelId + "/members/" + userId + "/permissions", data, null);
    }

    /**
     * 移除子频道权限
     *
     * @param channelId   子频道ID
     * @param userId      用户ID
     * @param permissions 权限 <a href="https://bot.q.qq.com/wiki/develop/api/openapi/channel_permissions/model.html#permissions">权限列表</a>
     */
    public void removeChannelPermissions(String channelId, String userId, Integer permissions) {
        Map<String, Object> data = new HashMap<>();
        data.put("remove", permissions.toString());
        put("/channels/" + channelId + "/members/" + userId + "/permissions", data, null);
    }

    /**
     * 获取子频道身份组权限
     *
     * @param channelId 子频道ID
     * @param roleId    身份组ID
     * @return {@link ChannelPermissions} 对象
     */
    public ChannelPermissions getChannelRolePermissions(String channelId, String roleId) {
        return get("/channels/" + channelId + "/roles/" + roleId + "/permissions", ChannelPermissions.class);
    }

    /**
     * 添加子频道身份组权限
     *
     * @param channelId   子频道ID
     * @param roleId      身份组ID
     * @param permissions 权限 <a href="https://bot.q.qq.com/wiki/develop/api/openapi/channel_permissions/model.html#permissions">权限列表</a>
     */
    public void addChannelRolePermissions(String channelId, String roleId, Integer permissions) {
        Map<String, Object> data = new HashMap<>();
        data.put("add", permissions.toString());
        put("/channels/" + channelId + "/roles/" + roleId + "/permissions", data, null);
    }

    /**
     * 移除子频道身份组权限
     *
     * @param channelId   子频道ID
     * @param roleId      身份组ID
     * @param permissions 权限 <a href="https://bot.q.qq.com/wiki/develop/api/openapi/channel_permissions/model.html#permissions">权限列表</a>
     */
    public void removeChannelRolePermissions(String channelId, String roleId, Integer permissions) {
        Map<String, Object> data = new HashMap<>();
        data.put("remove", permissions.toString());
        put("/channels/" + channelId + "/roles/" + roleId + "/permissions", data, null);
    }
}
